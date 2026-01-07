package com.dirijable.springstarter.financetracker.dto.transaction;

import com.dirijable.springstarter.financetracker.database.entity.TransactionType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionCreateDto(
        @NotNull Long accountId,
        @NotNull @Min(value = 0) BigDecimal amount,
        @Size(max = 500) String description,
        @PastOrPresent Instant transactionDate,
        @NotNull TransactionType transactionType,
        @NotNull Long categoryId
) {
}
