package com.company.employeesalary.employee.mapper;

import com.company.employeesalary.bank.mapper.BankAccountMapper;
import com.company.employeesalary.employee.dto.EmployeeRequestDto;
import com.company.employeesalary.employee.dto.EmployeeResponseDto;
import com.company.employeesalary.employee.dto.EmployeeUpdateDto;
import com.company.employeesalary.employee.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {BankAccountMapper.class})
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Employee toEntity(EmployeeRequestDto dto);

    @Mapping(target = "bankAccountNumber", source = "bankAccount.accountNumber")
    @Mapping(target = "maskedBankAccountNumber", source = "bankAccount.accountNumber", qualifiedByName = "maskAccountNumber")
    EmployeeResponseDto toResponseDto(Employee entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "bankAccount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(EmployeeUpdateDto dto, @MappingTarget Employee entity);
}
