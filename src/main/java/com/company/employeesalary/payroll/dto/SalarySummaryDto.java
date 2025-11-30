// SalarySummaryDto.java
package com.company.employeesalary.payroll.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class SalarySummaryDto {
    private BigDecimal totalPaid;
    private int paidCount;
    private int failedCount;
    private BigDecimal remainingCompanyBalance;
    private List<SalarySlipDto> salarySlips;
}