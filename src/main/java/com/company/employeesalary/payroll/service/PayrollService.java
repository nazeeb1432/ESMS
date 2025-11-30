package com.company.employeesalary.payroll.service;

import com.company.employeesalary.bank.entity.BankAccount;
import com.company.employeesalary.bank.repository.BankAccountRepository;
import com.company.employeesalary.common.dto.SalaryStatus;
import com.company.employeesalary.common.exception.InsufficientBalanceException;
import com.company.employeesalary.common.exception.ResourceNotFoundException;
import com.company.employeesalary.company.entity.CompanyAccount;
import com.company.employeesalary.company.service.CompanyAccountService;
import com.company.employeesalary.employee.entity.Employee;
import com.company.employeesalary.employee.repository.EmployeeRepository;
import com.company.employeesalary.payroll.dto.SalaryComponents;
import com.company.employeesalary.payroll.dto.SalarySlipDto;
import com.company.employeesalary.payroll.dto.SalarySummaryDto;
import com.company.employeesalary.payroll.entity.SalaryRecord;
import com.company.employeesalary.payroll.mapper.SalaryRecordMapper;
import com.company.employeesalary.payroll.repository.SalaryRecordRepository;
import com.company.employeesalary.payroll.strategy.SalaryCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayrollService {

    private final EmployeeRepository employeeRepository;
    private final SalaryRecordRepository salaryRecordRepository;
    private final CompanyAccountService companyAccountService;
    private final BankAccountRepository bankAccountRepository;
    private final SalaryCalculator salaryCalculator;
    private final SalaryRecordMapper salaryRecordMapper;

    /**
     * Preview salary for a specific employee without processing payment
     */
    @Transactional(readOnly = true)
    public SalarySlipDto previewSalary(String employeeId) {
        log.info("Previewing salary for employee: {}", employeeId);

        Employee employee = employeeRepository.findByEmployeeIdWithBankAccount(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with ID: " + employeeId));

        SalaryComponents components = salaryCalculator.calculate(employee.getGrade());

        return SalarySlipDto.builder()
                .employeeId(employee.getEmployeeId())
                .employeeName(employee.getName())
                .grade(employee.getGrade())
                .basic(components.getBasic())
                .houseRent(components.getHouseRent())
                .medical(components.getMedical())
                .gross(components.getGross())
                .status(null) // Not processed yet
                .paidAt(null)
                .build();
    }

    /**
     * Process payroll for all employees or selected employees
     * This is the CRITICAL transaction-safe salary transfer method
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public SalarySummaryDto processPayroll(List<Long> employeeIds) {
        log.info("Starting payroll processing for {} employees",
                employeeIds == null ? "all" : employeeIds.size());

        // Fetch employees with bank accounts
        List<Employee> employees = fetchEmployees(employeeIds);

        if (employees.isEmpty()) {
            log.warn("No employees found for payroll processing");
            return createEmptySummary();
        }

        // Process each employee's salary
        List<SalarySlipDto> salarySlips = new ArrayList<>();
        BigDecimal totalPaid = BigDecimal.ZERO;
        int paidCount = 0;
        int failedCount = 0;

        for (Employee employee : employees) {
            try {
                SalarySlipDto slip = processEmployeeSalary(employee);
                salarySlips.add(slip);

                if (slip.getStatus() == SalaryStatus.PAID) {
                    totalPaid = totalPaid.add(slip.getGross());
                    paidCount++;
                } else {
                    failedCount++;
                }
            } catch (Exception e) {
                log.error("Error processing salary for employee {}: {}",
                        employee.getEmployeeId(), e.getMessage());
                failedCount++;
            }
        }

        // Get final company balance
        CompanyAccount companyAccount = companyAccountService.getCompanyAccountEntity();

        log.info("Payroll processing completed. Paid: {}, Failed: {}, Total amount: {}",
                paidCount, failedCount, totalPaid);

        return SalarySummaryDto.builder()
                .totalPaid(totalPaid)
                .paidCount(paidCount)
                .failedCount(failedCount)
                .remainingCompanyBalance(companyAccount.getCurrentBalance())
                .salarySlips(salarySlips)
                .build();
    }

    /**
     * Process salary for a single employee with pessimistic locking
     * CRITICAL: This method ensures atomicity and consistency
     */
    private SalarySlipDto processEmployeeSalary(Employee employee) {
        log.debug("Processing salary for employee: {}", employee.getEmployeeId());

        // 1. Calculate salary components
        SalaryComponents components = salaryCalculator.calculate(employee.getGrade());
        BigDecimal grossSalary = components.getGross();

        // 2. Fetch company account with pessimistic write lock
        CompanyAccount companyAccount = companyAccountService.getCompanyAccountEntityWithLock();
        BigDecimal companyBalanceBefore = companyAccount.getCurrentBalance();

        // 3. Check if company has sufficient balance
        if (!companyAccount.hasSufficientBalance(grossSalary)) {
            log.warn("Insufficient company balance for employee {}. Required: {}, Available: {}",
                    employee.getEmployeeId(), grossSalary, companyBalanceBefore);

            // Create failed salary record
            SalaryRecord failedRecord = createSalaryRecord(
                    employee, components, companyBalanceBefore, null, SalaryStatus.FAILED);
            salaryRecordRepository.save(failedRecord);

            BigDecimal requiredTopUp = grossSalary.subtract(companyBalanceBefore);
            throw new InsufficientBalanceException(grossSalary, companyBalanceBefore, requiredTopUp);
        }

        // 4. Fetch employee bank account with lock
        BankAccount employeeBankAccount = bankAccountRepository.findByIdWithLock(employee.getBankAccount().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bank account not found for employee: " + employee.getEmployeeId()));

        // 5. Transfer money atomically
        try {
            companyAccount.debit(grossSalary);
            employeeBankAccount.credit(grossSalary);

            BigDecimal companyBalanceAfter = companyAccount.getCurrentBalance();

            // 6. Create successful salary record
            SalaryRecord paidRecord = createSalaryRecord(
                    employee, components, companyBalanceBefore, companyBalanceAfter, SalaryStatus.PAID);
            paidRecord.setPaidAt(LocalDateTime.now());

            salaryRecordRepository.save(paidRecord);

            log.info("Salary paid successfully for employee {}. Amount: {}",
                    employee.getEmployeeId(), grossSalary);

            return salaryRecordMapper.toSalarySlipDto(paidRecord);

        } catch (Exception e) {
            log.error("Error during salary transfer for employee {}: {}",
                    employee.getEmployeeId(), e.getMessage());

            // Create failed salary record
            SalaryRecord failedRecord = createSalaryRecord(
                    employee, components, companyBalanceBefore, null, SalaryStatus.FAILED);
            salaryRecordRepository.save(failedRecord);

            throw e;
        }
    }

    private List<Employee> fetchEmployees(List<Long> employeeIds) {
        if (employeeIds == null || employeeIds.isEmpty()) {
            return employeeRepository.findAllWithBankAccount();
        } else {
            return employeeRepository.findAllByIdInWithBankAccount(employeeIds);
        }
    }

    private SalaryRecord createSalaryRecord(Employee employee, SalaryComponents components,
                                            BigDecimal companyBalanceBefore,
                                            BigDecimal companyBalanceAfter,
                                            SalaryStatus status) {
        return SalaryRecord.builder()
                .employee(employee)
                .basicSalary(components.getBasic())
                .houseRent(components.getHouseRent())
                .medicalAllowance(components.getMedical())
                .grossSalary(components.getGross())
                .companyBalanceBefore(companyBalanceBefore)
                .companyBalanceAfter(companyBalanceAfter)
                .status(status)
                .build();
    }

    private SalarySummaryDto createEmptySummary() {
        CompanyAccount companyAccount = companyAccountService.getCompanyAccountEntity();

        return SalarySummaryDto.builder()
                .totalPaid(BigDecimal.ZERO)
                .paidCount(0)
                .failedCount(0)
                .remainingCompanyBalance(companyAccount.getCurrentBalance())
                .salarySlips(new ArrayList<>())
                .build();
    }

    /**
     * Get salary records for a specific employee
     */
    @Transactional(readOnly = true)
    public List<SalarySlipDto> getEmployeeSalaryHistory(String employeeId) {
        log.debug("Fetching salary history for employee: {}", employeeId);

        List<SalaryRecord> records = salaryRecordRepository
                .findByEmployeeEmployeeIdOrderByCreatedAtDesc(employeeId);

        return records.stream()
                .map(salaryRecordMapper::toSalarySlipDto)
                .toList();
    }

    /**
     * Get salary sheet for a specific date
     */
    @Transactional(readOnly = true)
    public List<SalarySlipDto> getSalarySheet(LocalDate date) {
        log.debug("Fetching salary sheet for date: {}", date);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<SalaryRecord> records = salaryRecordRepository
                .findByPaidAtBetweenOrderByPaidAtDesc(startOfDay, endOfDay);

        return records.stream()
                .map(salaryRecordMapper::toSalarySlipDto)
                .toList();
    }

    /**
     * Get summary report for a date range
     */
    @Transactional(readOnly = true)
    public SalarySummaryDto getSummaryReport(LocalDate fromDate, LocalDate toDate) {
        log.debug("Generating summary report from {} to {}", fromDate, toDate);

        LocalDateTime startDateTime = fromDate.atStartOfDay();
        LocalDateTime endDateTime = toDate.atTime(LocalTime.MAX);

        List<SalaryRecord> records = salaryRecordRepository
                .findByPaidAtBetweenOrderByPaidAtDesc(startDateTime, endDateTime);

        BigDecimal totalPaid = records.stream()
                .filter(r -> r.getStatus() == SalaryStatus.PAID)
                .map(SalaryRecord::getGrossSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long paidCount = records.stream()
                .filter(r -> r.getStatus() == SalaryStatus.PAID)
                .count();

        long failedCount = records.stream()
                .filter(r -> r.getStatus() == SalaryStatus.FAILED)
                .count();

        List<SalarySlipDto> slips = records.stream()
                .map(salaryRecordMapper::toSalarySlipDto)
                .toList();

        CompanyAccount companyAccount = companyAccountService.getCompanyAccountEntity();

        return SalarySummaryDto.builder()
                .totalPaid(totalPaid)
                .paidCount((int) paidCount)
                .failedCount((int) failedCount)
                .remainingCompanyBalance(companyAccount.getCurrentBalance())
                .salarySlips(slips)
                .build();
    }
}