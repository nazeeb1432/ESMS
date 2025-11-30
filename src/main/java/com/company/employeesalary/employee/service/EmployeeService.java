package com.company.employeesalary.employee.service;

import com.company.employeesalary.bank.repository.BankAccountRepository;
import com.company.employeesalary.common.exception.DuplicateResourceException;
import com.company.employeesalary.common.exception.ResourceNotFoundException;
import com.company.employeesalary.common.util.EmployeeIdGenerator;
import com.company.employeesalary.employee.dto.EmployeeRequestDto;
import com.company.employeesalary.employee.dto.EmployeeResponseDto;
import com.company.employeesalary.employee.dto.EmployeeUpdateDto;
import com.company.employeesalary.employee.entity.Employee;
import com.company.employeesalary.employee.mapper.EmployeeMapper;
import com.company.employeesalary.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BankAccountRepository bankAccountRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeIdGenerator employeeIdGenerator;

    @Transactional
    public EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto) {
        log.info("Creating new employee: {}", requestDto.getName());

        // Validate unique bank account number
        if (bankAccountRepository.existsByAccountNumber(requestDto.getBankAccount().getAccountNumber())) {
            throw new DuplicateResourceException(
                    "Bank account number already exists: " + requestDto.getBankAccount().getAccountNumber());
        }

        // Generate unique employee ID
        String employeeId = employeeIdGenerator.generateUniqueEmployeeId(
                employeeRepository::existsByEmployeeId);

        // Map and save employee
        Employee employee = employeeMapper.toEntity(requestDto);
        employee.setEmployeeId(employeeId);

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getEmployeeId());

        return employeeMapper.toResponseDto(savedEmployee);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeById(String employeeId) {
        log.debug("Fetching employee with ID: {}", employeeId);

        Employee employee = employeeRepository.findByEmployeeIdWithBankAccount(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with ID: " + employeeId));

        return employeeMapper.toResponseDto(employee);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDto> getAllEmployees(Pageable pageable) {
        log.debug("Fetching all employees with pagination");

        return employeeRepository.findAll(pageable)
                .map(employeeMapper::toResponseDto);
    }

    @Transactional
    public EmployeeResponseDto updateEmployee(String employeeId, EmployeeUpdateDto updateDto) {
        log.info("Updating employee: {}", employeeId);

        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with ID: " + employeeId));

        employeeMapper.updateEntityFromDto(updateDto, employee);

        Employee updatedEmployee = employeeRepository.save(employee);
        log.info("Employee updated successfully: {}", employeeId);

        return employeeMapper.toResponseDto(updatedEmployee);
    }

    @Transactional
    public void deleteEmployee(String employeeId) {
        log.info("Deleting employee: {}", employeeId);

        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with ID: " + employeeId));

        employeeRepository.delete(employee);
        log.info("Employee deleted successfully: {}", employeeId);
    }
}
