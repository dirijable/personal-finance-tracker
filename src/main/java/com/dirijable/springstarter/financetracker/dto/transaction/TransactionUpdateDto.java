package com.dirijable.springstarter.financetracker.dto.transaction;

import com.dirijable.springstarter.financetracker.database.entity.TransactionType;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransactionUpdateDto(
        @Positive BigDecimal amount,
        @Size(max = 500) String description,
        TransactionType transactionType,
        Long accountId,
        Long categoryId
) {}
