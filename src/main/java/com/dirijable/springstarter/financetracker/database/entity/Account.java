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
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(value = EnumType.STRING)
    Currency currency;

    public void updateBalance(BigDecimal amount, TransactionType transactionType ){
        if(transactionType == TransactionType.INCOME){
            balance = balance.add(amount);
        }
        else if(transactionType == TransactionType.EXPENSE){
           balance = balance.subtract(amount);
        }
    }

    public void reverseTransaction(BigDecimal amount, TransactionType transactionType){
        if(transactionType == TransactionType.INCOME){
            balance = balance.subtract(amount);
        } else if (transactionType == TransactionType.EXPENSE) {
            balance = balance.add(amount);
        }
    }
}
