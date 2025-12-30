package com.dirijable.springstarter.financetracker.mapper;

import com.dirijable.springstarter.financetracker.database.entity.Account;
import com.dirijable.springstarter.financetracker.dto.account.AccountCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AccountMapper {

    public Account toEntity(AccountCreateDto createDto);
}
