package com.dirijable.springstarter.financetracker.repository;

import com.dirijable.springstarter.financetracker.database.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
