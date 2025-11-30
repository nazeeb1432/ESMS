// CompanyAccountResponseDto.java
package com.company.employeesalary.company.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CompanyAccountResponseDto {
    private Long id;
    private String accountName;
    private String accountNumber;
    private BigDecimal currentBalance;
    private String bankName;
    private String branchName;
    private LocalDateTime updatedAt;
}