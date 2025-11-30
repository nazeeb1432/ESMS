package com.company.employeesalary.company.controller;

import com.company.employeesalary.company.dto.CompanyAccountResponseDto;
import com.company.employeesalary.company.dto.TopUpRequestDto;
import com.company.employeesalary.company.service.CompanyAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company/account")
@RequiredArgsConstructor
@Tag(name = "Company Account", description = "APIs for managing company bank account")
public class CompanyAccountController {

    private final CompanyAccountService companyAccountService;

    @GetMapping
    @Operation(summary = "Get company account", description = "Retrieves company account details and balance")
    public ResponseEntity<CompanyAccountResponseDto> getCompanyAccount() {
        CompanyAccountResponseDto response = companyAccountService.getCompanyAccount();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/topup")
    @Operation(summary = "Top-up company account", description = "Adds funds to company account")
    public ResponseEntity<CompanyAccountResponseDto> topUpAccount(
            @Valid @RequestBody TopUpRequestDto requestDto) {
        CompanyAccountResponseDto response = companyAccountService.topUpAccount(requestDto);
        return ResponseEntity.ok(response);
    }
}
