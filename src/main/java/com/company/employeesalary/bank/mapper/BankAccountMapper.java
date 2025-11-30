package com.company.employeesalary.bank.mapper;

import com.company.employeesalary.bank.dto.BankAccountDto;
import com.company.employeesalary.bank.dto.BankAccountResponseDto;
import com.company.employeesalary.bank.entity.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currentBalance", constant = "0")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BankAccount toEntity(BankAccountDto dto);

    @Mapping(target = "maskedAccountNumber", source = "accountNumber", qualifiedByName = "maskAccountNumber")
    BankAccountResponseDto toResponseDto(BankAccount entity);

    @Named("maskAccountNumber")
    default String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() <= 4) {
            return accountNumber;
        }
        int visibleDigits = 4;
        int maskedLength = accountNumber.length() - visibleDigits;
        return "*".repeat(maskedLength) + accountNumber.substring(maskedLength);
    }
}
