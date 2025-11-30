// SalaryConfigurationController.java
package com.company.employeesalary.configuration.controller;

import com.company.employeesalary.configuration.dto.SalaryConfigurationResponseDto;
import com.company.employeesalary.configuration.dto.SalaryConfigurationUpdateDto;
import com.company.employeesalary.configuration.service.SalaryConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuration/salary")
@RequiredArgsConstructor
@Tag(name = "Salary Configuration", description = "APIs for managing salary calculation rules")
public class SalaryConfigurationController {

    private final SalaryConfigurationService service;

    @GetMapping
    @Operation(summary = "Get salary configuration",
            description = "Retrieves current salary calculation configuration")
    public ResponseEntity<SalaryConfigurationResponseDto> getConfiguration() {
        SalaryConfigurationResponseDto response = service.getConfiguration();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @Operation(summary = "Update salary configuration",
            description = "Updates salary calculation rules including base salary and increments")
    public ResponseEntity<SalaryConfigurationResponseDto> updateConfiguration(
            @Valid @RequestBody SalaryConfigurationUpdateDto updateDto) {
        SalaryConfigurationResponseDto response = service.updateConfiguration(updateDto);
        return ResponseEntity.ok(response);
    }
}