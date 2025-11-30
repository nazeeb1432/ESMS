// PayrollProcessRequestDto.java
package com.company.employeesalary.payroll.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PayrollProcessRequestDto {
    private List<Long> employeeIds; // null or empty means process all
}