// SalaryComponents.java
package com.company.employeesalary.payroll.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SalaryComponents {
    private BigDecimal basic;
    private BigDecimal houseRent;
    private BigDecimal medical;
    private BigDecimal gross;
}
