package com.dirijable.springstarter.financetracker.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    BigDecimal amount;

    String description;

    Instant transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id",
            nullable = false)
    Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,
            length = 64)
    TransactionType transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",
            nullable = false)
    Category category;

}
