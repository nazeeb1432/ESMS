package com.company.employeesalary.payroll.controller;

import com.company.employeesalary.payroll.dto.SalarySlipDto;
import com.company.employeesalary.payroll.dto.SalarySummaryDto;
import com.company.employeesalary.payroll.service.PayrollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/payroll")
@RequiredArgsConstructor
@Tag(name = "Payroll Management", description = "APIs for payroll processing and salary management")
public class PayrollController {

    private final PayrollService payrollService;

    @GetMapping("/salary/{employeeId}")
    @Operation(summary = "Preview employee salary",
            description = "Calculate and preview salary for an employee without processing payment")
    public ResponseEntity<SalarySlipDto> previewSalary(@PathVariable String employeeId) {
        SalarySlipDto response = payrollService.previewSalary(employeeId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/process")
    @Operation(summary = "Process payroll",
            description = "Process salary payments for all employees or selected employees")
    public ResponseEntity<SalarySummaryDto> processPayroll(
            @RequestParam(required = false) List<Long> ids) {
        SalarySummaryDto response = payrollService.processPayroll(ids);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{employeeId}")
    @Operation(summary = "Get salary history",
            description = "Retrieve salary payment history for a specific employee")
    public ResponseEntity<List<SalarySlipDto>> getSalaryHistory(@PathVariable String employeeId) {
        List<SalarySlipDto> response = payrollService.getEmployeeSalaryHistory(employeeId);
        return ResponseEntity.ok(response);
    }
}
