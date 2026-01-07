package com.dirijable.springstarter.financetracker.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;


@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true,
            nullable = false,
            length = 64)
    String name;

    @Column(length = 500)
    String description;

    @Builder.Default
    @Column(nullable = false)
    BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            nullable = false)
    User user;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false,
            length = 3)
    Currency currency;

    public void updateBalance(BigDecimal amount, TransactionType transactionType) {
        if (transactionType == TransactionType.INCOME) {
            balance = balance.add(amount);
        } else if (transactionType == TransactionType.EXPENSE) {
            balance = balance.subtract(amount);
        }
    }

    public void reverseTransaction(BigDecimal amount, TransactionType transactionType) {
        if (transactionType == TransactionType.INCOME) {
            balance = balance.subtract(amount);
        } else if (transactionType == TransactionType.EXPENSE) {
            balance = balance.add(amount);
        }
    }
}
