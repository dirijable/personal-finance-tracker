package com.dirijable.springstarter.financetracker.repository;

import com.dirijable.springstarter.financetracker.database.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
