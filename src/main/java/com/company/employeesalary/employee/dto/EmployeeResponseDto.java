// EmployeeResponseDto.java
package com.company.employeesalary.employee.dto;

import com.company.employeesalary.common.dto.Grade;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EmployeeResponseDto {
    private Long id;
    private String employeeId;
    private String name;
    private Grade grade;
    private String address;
    private String mobileNumber;
    private String bankAccountNumber;
    private String maskedBankAccountNumber;
    private LocalDateTime createdAt;
}
