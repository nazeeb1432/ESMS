package com.company.employeesalary.payroll.entity;

import com.company.employeesalary.common.dto.SalaryStatus;
import com.company.employeesalary.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "basic_salary", nullable = false, precision = 15, scale = 2)
    private BigDecimal basicSalary;

    @Column(name = "house_rent", nullable = false, precision = 15, scale = 2)
    private BigDecimal houseRent;

    @Column(name = "medical_allowance", nullable = false, precision = 15, scale = 2)
    private BigDecimal medicalAllowance;

    @Column(name = "gross_salary", nullable = false, precision = 15, scale = 2)
    private BigDecimal grossSalary;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SalaryStatus status;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "company_balance_before", nullable = false, precision = 15, scale = 2)
    private BigDecimal companyBalanceBefore;

    @Column(name = "company_balance_after", precision = 15, scale = 2)
    private BigDecimal companyBalanceAfter;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}