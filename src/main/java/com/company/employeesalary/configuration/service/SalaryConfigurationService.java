// SalaryConfigurationService.java
package com.company.employeesalary.configuration.service;

import com.company.employeesalary.common.exception.ResourceNotFoundException;
import com.company.employeesalary.configuration.dto.SalaryConfigurationResponseDto;
import com.company.employeesalary.configuration.dto.SalaryConfigurationUpdateDto;
import com.company.employeesalary.configuration.entity.SalaryConfiguration;
import com.company.employeesalary.configuration.mapper.SalaryConfigurationMapper;
import com.company.employeesalary.configuration.repository.SalaryConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalaryConfigurationService {

    private final SalaryConfigurationRepository repository;
    private final SalaryConfigurationMapper mapper;

    @Transactional(readOnly = true)
    public SalaryConfigurationResponseDto getConfiguration() {
        log.debug("Fetching salary configuration");

        SalaryConfiguration config = repository.findFirstConfiguration()
                .orElseThrow(() -> new ResourceNotFoundException("Salary configuration not found"));

        return mapper.toResponseDto(config);
    }

    @Transactional
    public SalaryConfigurationResponseDto updateConfiguration(SalaryConfigurationUpdateDto updateDto) {
        log.info("Updating salary configuration");

        SalaryConfiguration config = repository.findFirstConfiguration()
                .orElseThrow(() -> new ResourceNotFoundException("Salary configuration not found"));

        mapper.updateEntityFromDto(updateDto, config);

        SalaryConfiguration updatedConfig = repository.save(config);

        log.info("Salary configuration updated successfully. Base salary: {}, Grade increment: {}",
                updatedConfig.getBaseSalary(), updatedConfig.getGradeIncrement());

        return mapper.toResponseDto(updatedConfig);
    }

    @Transactional(readOnly = true)
    public SalaryConfiguration getConfigurationEntity() {
        return repository.findFirstConfiguration()
                .orElseThrow(() -> new ResourceNotFoundException("Salary configuration not found"));
    }

    // Helper methods for salary calculator
    public BigDecimal getBaseSalary() {
        return getConfigurationEntity().getBaseSalary();
    }

    public BigDecimal getGradeIncrement() {
        return getConfigurationEntity().getGradeIncrement();
    }

    public Integer getHouseRentPercentage() {
        return getConfigurationEntity().getHouseRentPercentage();
    }

    public Integer getMedicalPercentage() {
        return getConfigurationEntity().getMedicalPercentage();
    }
}