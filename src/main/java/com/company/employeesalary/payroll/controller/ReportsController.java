// ReportsController.java
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
@RequestMapping("/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "APIs for generating salary reports and summaries")
public class ReportsController {

    private final PayrollService payrollService;

    @GetMapping("/salary-sheet")
    @Operation(summary = "Get salary sheet",
            description = "Generate salary sheet for a specific date")
    public ResponseEntity<List<SalarySlipDto>> getSalarySheet(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<SalarySlipDto> response = payrollService.getSalarySheet(date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    @Operation(summary = "Get summary report",
            description = "Generate summary report for a date range")
    public ResponseEntity<SalarySummaryDto> getSummaryReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        SalarySummaryDto response = payrollService.getSummaryReport(from, to);
        return ResponseEntity.ok(response);
    }
}
