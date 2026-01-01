package com.dirijable.springstarter.financetracker.dto.account;

import com.dirijable.springstarter.financetracker.database.entity.Currency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AccountCreateDto(
        @NotBlank String name,
        @Size(max = 500) String description,
        @NotBlank @Min(value = 0) BigDecimal balance,
        @NotBlank Currency currency
) {}
