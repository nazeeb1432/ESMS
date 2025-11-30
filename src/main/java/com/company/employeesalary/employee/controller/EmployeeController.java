package com.company.employeesalary.employee.controller;

import com.company.employeesalary.employee.dto.EmployeeRequestDto;
import com.company.employeesalary.employee.dto.EmployeeResponseDto;
import com.company.employeesalary.employee.dto.EmployeeUpdateDto;
import com.company.employeesalary.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @Operation(summary = "Create new employee", description = "Creates a new employee with bank account")
    public ResponseEntity<EmployeeResponseDto> createEmployee(
            @Valid @RequestBody EmployeeRequestDto requestDto) {
        EmployeeResponseDto response = employeeService.createEmployee(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{employeeId}")
    @Operation(summary = "Get employee by ID", description = "Retrieves employee details by employee ID")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(
            @PathVariable String employeeId) {
        EmployeeResponseDto response = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all employees", description = "Retrieves paginated list of all employees")
    public ResponseEntity<Page<EmployeeResponseDto>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeeResponseDto> response = employeeService.getAllEmployees(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{employeeId}")
    @Operation(summary = "Update employee", description = "Updates employee information")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @PathVariable String employeeId,
            @Valid @RequestBody EmployeeUpdateDto updateDto) {
        EmployeeResponseDto response = employeeService.updateEmployee(employeeId, updateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{employeeId}")
    @Operation(summary = "Delete employee", description = "Deletes an employee by ID")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }
}
