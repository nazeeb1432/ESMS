package com.company.employeesalary.company.repository;

import com.company.employeesalary.company.entity.CompanyAccount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyAccountRepository extends JpaRepository<CompanyAccount, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ca FROM CompanyAccount ca WHERE ca.id = :id")
    Optional<CompanyAccount> findByIdWithLock(Long id);

    @Query("SELECT ca FROM CompanyAccount ca ORDER BY ca.id LIMIT 1")
    Optional<CompanyAccount> findFirstCompanyAccount();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ca FROM CompanyAccount ca ORDER BY ca.id LIMIT 1")
    Optional<CompanyAccount> findFirstCompanyAccountWithLock();
}
