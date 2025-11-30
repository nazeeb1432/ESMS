package com.company.employeesalary.common.exception;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class InsufficientBalanceException extends RuntimeException {
    private final BigDecimal requiredAmount;
    private final BigDecimal availableBalance;
    private final BigDecimal requiredTopUp;

    public InsufficientBalanceException(BigDecimal requiredAmount,
                                        BigDecimal availableBalance,
                                        BigDecimal requiredTopUp) {
        super(String.format("Insufficient balance. Required: %s, Available: %s, Top-up needed: %s",
                requiredAmount, availableBalance, requiredTopUp));
        this.requiredAmount = requiredAmount;
        this.availableBalance = availableBalance;
        this.requiredTopUp = requiredTopUp;
    }
}