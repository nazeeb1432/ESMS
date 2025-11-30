package com.company.employeesalary.employee.repository;

import com.company.employeesalary.common.dto.Grade;
import com.company.employeesalary.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmployeeId(String employeeId);

    Optional<Employee> findByEmployeeId(String employeeId);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.bankAccount WHERE e.employeeId = :employeeId")
    Optional<Employee> findByEmployeeIdWithBankAccount(String employeeId);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.bankAccount")
    List<Employee> findAllWithBankAccount();

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.bankAccount WHERE e.id IN :ids")
    List<Employee> findAllByIdInWithBankAccount(List<Long> ids);

    List<Employee> findByGrade(Grade grade);

    Page<Employee> findAll(Pageable pageable);
}
