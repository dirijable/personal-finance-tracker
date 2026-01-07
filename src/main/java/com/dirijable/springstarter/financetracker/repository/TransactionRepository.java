package com.dirijable.springstarter.financetracker.repository;

import com.dirijable.springstarter.financetracker.database.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(
            value = "SELECT t FROM Transaction t " +
                    "WHERE t.id = :id AND t.account.user.id = :userId"
    )
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    List<Transaction> findAllByAccountUserId(Long userId);
}
