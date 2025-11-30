// SalaryConfigurationRepository.java
package com.company.employeesalary.configuration.repository;

import com.company.employeesalary.configuration.entity.SalaryConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalaryConfigurationRepository extends JpaRepository<SalaryConfiguration, Long> {

    @Query("SELECT sc FROM SalaryConfiguration sc ORDER BY sc.id LIMIT 1")
    Optional<SalaryConfiguration> findFirstConfiguration();
}