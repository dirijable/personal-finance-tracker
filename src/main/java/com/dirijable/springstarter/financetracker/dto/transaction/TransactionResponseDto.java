package com.dirijable.springstarter.financetracker.dto.transaction;

import com.dirijable.springstarter.financetracker.database.entity.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
TODO в будщем добавить вместо accountId и categoryId какой-нибудь там AccountInfo и CategoryInfo
 */
public record TransactionResponseDto(
        Long id,
        LocalDateTime createdAt,
        BigDecimal amount,
        String description,
        TransactionType transactionType,
        Long accountId,
        Long categoryId) {
}
