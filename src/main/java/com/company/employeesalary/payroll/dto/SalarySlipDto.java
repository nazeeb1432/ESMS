// SalarySlipDto.java
package com.company.employeesalary.payroll.dto;

import com.company.employeesalary.common.dto.Grade;
import com.company.employeesalary.common.dto.SalaryStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SalarySlipDto {
    private String employeeId;
    private String employeeName;
    private Grade grade;
    private BigDecimal basic;
    private BigDecimal houseRent;
    private BigDecimal medical;
    private BigDecimal gross;
    private SalaryStatus status;
    private LocalDateTime paidAt;
}