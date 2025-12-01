// EmployeeRequestDto.java
package com.company.employeesalary.employee.dto;

import com.company.employeesalary.bank.dto.BankAccountDto;
import com.company.employeesalary.common.dto.Grade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Grade is required")
    private Grade grade;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid mobile number format")
    private String mobileNumber;

    @NotNull(message = "Bank account is required")
    @Valid
    private BankAccountDto bankAccount;
}
