// BankAccountResponseDto.java
package com.company.employeesalary.bank.dto;

import com.company.employeesalary.common.dto.AccountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BankAccountResponseDto {
    private Long id;
    private String accountName;
    private String accountNumber;
    private String maskedAccountNumber;
    private AccountType accountType;
    private BigDecimal currentBalance;
    private String bankName;
    private String branchName;
}
