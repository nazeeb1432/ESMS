// SalaryConfigurationMapper.java
package com.company.employeesalary.configuration.mapper;

import com.company.employeesalary.configuration.dto.SalaryConfigurationResponseDto;
import com.company.employeesalary.configuration.dto.SalaryConfigurationUpdateDto;
import com.company.employeesalary.configuration.entity.SalaryConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SalaryConfigurationMapper {

    SalaryConfigurationResponseDto toResponseDto(SalaryConfiguration entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(SalaryConfigurationUpdateDto dto, @MappingTarget SalaryConfiguration entity);
}