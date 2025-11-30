// SalaryConfigurationResponseDto.java
package com.company.employeesalary.configuration.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SalaryConfigurationResponseDto {
    private Long id;
    private BigDecimal baseSalary;
    private BigDecimal gradeIncrement;
    private Integer houseRentPercentage;
    private Integer medicalPercentage;
    private LocalDateTime updatedAt;
}
