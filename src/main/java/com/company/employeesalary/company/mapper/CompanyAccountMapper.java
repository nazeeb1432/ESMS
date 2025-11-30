package com.company.employeesalary.company.mapper;

import com.company.employeesalary.company.dto.CompanyAccountResponseDto;
import com.company.employeesalary.company.entity.CompanyAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyAccountMapper {

    CompanyAccountResponseDto toResponseDto(CompanyAccount entity);
}
