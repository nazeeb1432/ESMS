package com.company.employeesalary.payroll.repository;

import com.company.employeesalary.common.dto.SalaryStatus;
import com.company.employeesalary.payroll.entity.SalaryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalaryRecordRepository extends JpaRepository<SalaryRecord, Long> {

    @Query("SELECT sr FROM SalaryRecord sr JOIN FETCH sr.employee WHERE sr.employee.employeeId = :employeeId ORDER BY sr.createdAt DESC")
    List<SalaryRecord> findByEmployeeEmployeeIdOrderByCreatedAtDesc(String employeeId);

    @Query("SELECT sr FROM SalaryRecord sr JOIN FETCH sr.employee WHERE sr.paidAt BETWEEN :startDate AND :endDate ORDER BY sr.paidAt DESC")
    List<SalaryRecord> findByPaidAtBetweenOrderByPaidAtDesc(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT sr FROM SalaryRecord sr JOIN FETCH sr.employee WHERE sr.status = :status ORDER BY sr.createdAt DESC")
    List<SalaryRecord> findByStatusOrderByCreatedAtDesc(SalaryStatus status);

    @Query("SELECT sr FROM SalaryRecord sr JOIN FETCH sr.employee ORDER BY sr.createdAt DESC")
    List<SalaryRecord> findAllWithEmployee();
}