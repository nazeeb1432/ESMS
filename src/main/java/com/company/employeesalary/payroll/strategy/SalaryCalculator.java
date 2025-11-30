package com.company.employeesalary.payroll.strategy;

import com.company.employeesalary.common.dto.Grade;
import com.company.employeesalary.payroll.dto.SalaryComponents;

public interface SalaryCalculator {
    /**
     * Calculate salary components for a given grade
     * @param grade Employee grade
     * @return Salary components (basic, house rent, medical, gross)
     */
    SalaryComponents calculate(Grade grade);
}
