package com.dirijable.springstarter.financetracker.dto.account;

import com.dirijable.springstarter.financetracker.database.entity.Currency;

import java.math.BigDecimal;

public record AccountResponseDto(
        Long id,
        String name,
        String description,
        BigDecimal balance,
        Currency currency
) {
}
