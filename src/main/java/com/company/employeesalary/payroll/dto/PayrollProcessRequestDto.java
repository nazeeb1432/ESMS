// PayrollProcessRequestDto.java
package com.company.employeesalary.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayrollProcessRequestDto {
    private List<Long> employeeIds; // null or empty means process all
}