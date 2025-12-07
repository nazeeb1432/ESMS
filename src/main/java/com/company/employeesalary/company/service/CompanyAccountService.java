package com.company.employeesalary.company.service;

import com.company.employeesalary.common.exception.InvalidOperationException;
import com.company.employeesalary.common.exception.ResourceNotFoundException;
import com.company.employeesalary.company.dto.CompanyAccountResponseDto;
import com.company.employeesalary.company.dto.SetBalanceRequestDto;
import com.company.employeesalary.company.dto.TopUpRequestDto;
import com.company.employeesalary.company.entity.CompanyAccount;
import com.company.employeesalary.company.mapper.CompanyAccountMapper;
import com.company.employeesalary.company.repository.CompanyAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyAccountService {

    private final CompanyAccountRepository companyAccountRepository;
    private final CompanyAccountMapper companyAccountMapper;

    @Transactional(readOnly = true)
    public CompanyAccountResponseDto getCompanyAccount() {
        log.debug("Fetching company account");

        CompanyAccount account = companyAccountRepository.findFirstCompanyAccount()
                .orElseThrow(() -> new ResourceNotFoundException("Company account not found"));

        return companyAccountMapper.toResponseDto(account);
    }

    @Transactional
    public CompanyAccountResponseDto topUpAccount(TopUpRequestDto requestDto) {
        log.info("Processing top-up request for amount: {}", requestDto.getAmount());

        if (requestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("Top-up amount must be positive");
        }

        CompanyAccount account = companyAccountRepository.findFirstCompanyAccountWithLock()
                .orElseThrow(() -> new ResourceNotFoundException("Company account not found"));

        BigDecimal oldBalance = account.getCurrentBalance();
        account.credit(requestDto.getAmount());

        CompanyAccount updatedAccount = companyAccountRepository.save(account);

        log.info("Company account topped up successfully. Old balance: {}, New balance: {}",
                oldBalance, updatedAccount.getCurrentBalance());

        return companyAccountMapper.toResponseDto(updatedAccount);
    }

    @Transactional
    public CompanyAccountResponseDto setBalance(SetBalanceRequestDto requestDto) {
        log.info("Processing set balance request for amount: {}", requestDto.getAmount());

        if (requestDto.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOperationException("Balance amount cannot be negative");
        }

        CompanyAccount account = companyAccountRepository.findFirstCompanyAccountWithLock()
                .orElseThrow(() -> new ResourceNotFoundException("Company account not found"));

        BigDecimal oldBalance = account.getCurrentBalance();
        account.setCurrentBalance(requestDto.getAmount());

        CompanyAccount updatedAccount = companyAccountRepository.save(account);

        log.info("Company account balance set successfully. Old balance: {}, New balance: {}",
                oldBalance, updatedAccount.getCurrentBalance());

        return companyAccountMapper.toResponseDto(updatedAccount);
    }

    @Transactional(readOnly = true)
    public CompanyAccount getCompanyAccountEntity() {
        return companyAccountRepository.findFirstCompanyAccount()
                .orElseThrow(() -> new ResourceNotFoundException("Company account not found"));
    }

    @Transactional
    public CompanyAccount getCompanyAccountEntityWithLock() {
        return companyAccountRepository.findFirstCompanyAccountWithLock()
                .orElseThrow(() -> new ResourceNotFoundException("Company account not found"));
    }
}
