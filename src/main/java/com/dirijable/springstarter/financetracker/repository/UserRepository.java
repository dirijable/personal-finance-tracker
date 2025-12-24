package com.dirijable.springstarter.financetracker.repository;

import com.dirijable.springstarter.financetracker.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);
}
