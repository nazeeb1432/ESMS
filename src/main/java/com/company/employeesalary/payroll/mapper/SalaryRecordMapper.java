// SalaryRecordMapper.java
package com.company.employeesalary.payroll.mapper;

import com.company.employeesalary.payroll.dto.SalarySlipDto;
import com.company.employeesalary.payroll.entity.SalaryRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SalaryRecordMapper {

    @Mapping(target = "employeeId", source = "employee.employeeId")
    @Mapping(target = "employeeName", source = "employee.name")
    @Mapping(target = "grade", source = "employee.grade")
    @Mapping(target = "basic", source = "basicSalary")
    @Mapping(target = "houseRent", source = "houseRent")
    @Mapping(target = "medical", source = "medicalAllowance")
    @Mapping(target = "gross", source = "grossSalary")
    SalarySlipDto toSalarySlipDto(SalaryRecord salaryRecord);
}