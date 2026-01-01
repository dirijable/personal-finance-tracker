package com.dirijable.springstarter.financetracker.dto.account;

import com.dirijable.springstarter.financetracker.database.entity.Currency;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record AccountUpdateDto(
        String name,
        String description
) {
}
