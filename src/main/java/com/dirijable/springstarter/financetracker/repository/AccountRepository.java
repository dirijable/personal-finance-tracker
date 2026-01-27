package com.dirijable.springstarter.financetracker.repository;

import com.dirijable.springstarter.financetracker.database.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAllByUserId(Long userId);
    Page<Account> findAllByUserId(Long userId, Pageable pageable);
    boolean existsByNameAndUserId(String name, Long userId);
    boolean existsAccountByIdAndUserId(Long id, Long userId);
}
