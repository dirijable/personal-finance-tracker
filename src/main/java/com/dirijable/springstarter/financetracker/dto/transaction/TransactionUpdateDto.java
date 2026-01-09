package com.dirijable.springstarter.financetracker.dto.transaction;

import com.dirijable.springstarter.financetracker.database.entity.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransactionUpdateDto(
        @PositiveOrZero BigDecimal amount,
        @Size(max = 500) String description,
        TransactionType transactionType,
        Long accountId,
        Long categoryId
) {}
