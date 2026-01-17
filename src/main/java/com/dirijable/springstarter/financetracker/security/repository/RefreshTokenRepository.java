package com.dirijable.springstarter.financetracker.security.repository;

import com.dirijable.springstarter.financetracker.security.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    void deleteById(Long id);
    void deleteByToken(String token);
    void deleteByTokenAndUserId(String token, Long userId);
    void deleteAllByUserId(Long userId);
}
