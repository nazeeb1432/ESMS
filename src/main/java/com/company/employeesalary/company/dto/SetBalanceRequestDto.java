package com.company.employeesalary.company.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetBalanceRequestDto {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Amount must be zero or greater")
    private BigDecimal amount;
}

