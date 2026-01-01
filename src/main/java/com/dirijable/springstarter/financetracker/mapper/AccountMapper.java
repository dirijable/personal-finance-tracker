package com.dirijable.springstarter.financetracker.mapper;

import com.dirijable.springstarter.financetracker.database.entity.Account;
import com.dirijable.springstarter.financetracker.dto.account.AccountCreateDto;
import com.dirijable.springstarter.financetracker.dto.account.AccountResponseDto;
import com.dirijable.springstarter.financetracker.dto.account.AccountUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AccountMapper {


    Account toEntity(AccountCreateDto createDto);

    void updateEntity(AccountUpdateDto updateDto, @MappingTarget Account entity);

    AccountResponseDto toResponse(Account entity);
}
