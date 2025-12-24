package com.dirijable.springstarter.financetracker.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

}
