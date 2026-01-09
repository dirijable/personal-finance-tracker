package com.dirijable.springstarter.financetracker.dto.account;

import com.dirijable.springstarter.financetracker.database.entity.Currency;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AccountCreateDto(
        @NotBlank String name,
        @Size(max = 500) String description,
        @NotNull @PositiveOrZero BigDecimal balance,
        @NotNull Currency currency
) {}
