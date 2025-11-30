// SalaryConfig.java
package com.company.employeesalary.common.config;

import com.company.employeesalary.common.dto.Grade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

//deprecated, no need to use it, because we are using database to store salary configurations
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.salary")
public class SalaryConfig {
    private Grade baseGrade = Grade.GRADE_6;
    private BigDecimal baseSalary = new BigDecimal("25000");
    private BigDecimal gradeIncrement = new BigDecimal("5000");
    private int houseRentPercentage = 20;
    private int medicalPercentage = 15;
}
