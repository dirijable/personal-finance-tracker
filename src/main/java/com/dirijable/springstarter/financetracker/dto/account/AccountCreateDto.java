package com.dirijable.springstarter.financetracker.dto.account;

import com.dirijable.springstarter.financetracker.database.entity.Currency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record AccountCreateDto(
        @NotBlank String name,
        @NotBlank @Min(value = 0) BigDecimal balance,
        @NotBlank Currency currency
) {}
