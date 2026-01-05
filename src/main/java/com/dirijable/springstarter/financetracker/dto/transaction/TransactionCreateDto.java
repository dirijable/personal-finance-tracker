package com.dirijable.springstarter.financetracker.dto.transaction;

import com.dirijable.springstarter.financetracker.database.entity.TransactionType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TransactionCreateDto (
        @NotNull @Min(value = 0) BigDecimal amount,
        @Size(max = 500) String description,
        @NotNull TransactionType transactionType,
        @NotNull Long categoryId
        ) {}
