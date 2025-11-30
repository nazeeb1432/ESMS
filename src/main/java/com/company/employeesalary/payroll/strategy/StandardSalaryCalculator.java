// StandardSalaryCalculator.java
package com.company.employeesalary.payroll.strategy;

import com.company.employeesalary.common.dto.Grade;
import com.company.employeesalary.configuration.service.SalaryConfigurationService;
import com.company.employeesalary.payroll.dto.SalaryComponents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Component
@RequiredArgsConstructor
public class StandardSalaryCalculator implements SalaryCalculator {

    private final SalaryConfigurationService configService;

    @Override
    public SalaryComponents calculate(Grade grade) {
        log.debug("Calculating salary for grade: {}", grade);

        // Fetch configuration from database
        BigDecimal baseSalary = configService.getBaseSalary();
        BigDecimal gradeIncrement = configService.getGradeIncrement();
        Integer houseRentPercentage = configService.getHouseRentPercentage();
        Integer medicalPercentage = configService.getMedicalPercentage();

        // Calculate basic salary
        // Base salary for GRADE_6, add increment for each level above
        int stepsFromBase = grade.getStepsFromBase();
        BigDecimal increment = gradeIncrement.multiply(BigDecimal.valueOf(stepsFromBase));
        BigDecimal basic = baseSalary.add(increment);

        // Calculate house rent (configurable % of basic)
        BigDecimal houseRent = basic
                .multiply(BigDecimal.valueOf(houseRentPercentage))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        // Calculate medical allowance (configurable % of basic)
        BigDecimal medical = basic
                .multiply(BigDecimal.valueOf(medicalPercentage))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        // Calculate gross salary
        BigDecimal gross = basic.add(houseRent).add(medical);

        log.debug("Salary calculated for {}: Basic={}, HouseRent={}, Medical={}, Gross={}",
                grade, basic, houseRent, medical, gross);

        return SalaryComponents.builder()
                .basic(basic)
                .houseRent(houseRent)
                .medical(medical)
                .gross(gross)
                .build();
    }
}
