package com.company.employeesalary.configuration.dto;

// SalaryConfigurationUpdateDto.java

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SalaryConfigurationUpdateDto {

    @NotNull(message = "Base salary is required")
    @DecimalMin(value = "0.01", message = "Base salary must be greater than zero")
    private BigDecimal baseSalary;

    @NotNull(message = "Grade increment is required")
    @DecimalMin(value = "0.00", message = "Grade increment must be non-negative")
    private BigDecimal gradeIncrement;

    @NotNull(message = "House rent percentage is required")
    @Min(value = 0, message = "House rent percentage must be between 0 and 100")
    @Max(value = 100, message = "House rent percentage must be between 0 and 100")
    private Integer houseRentPercentage;

    @NotNull(message = "Medical percentage is required")
    @Min(value = 0, message = "Medical percentage must be between 0 and 100")
    @Max(value = 100, message = "Medical percentage must be between 0 and 100")
    private Integer medicalPercentage;
}
