package com.dirijable.springstarter.financetracker.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String card;
    private String currency; //ну кста возможно есть смысл сделать enum
}
