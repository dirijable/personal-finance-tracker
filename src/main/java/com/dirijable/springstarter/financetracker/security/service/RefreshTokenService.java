package com.dirijable.springstarter.financetracker.security.service;

import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.exception.business.notfound.UserNotFoundException;
import com.dirijable.springstarter.financetracker.repository.UserRepository;
import com.dirijable.springstarter.financetracker.security.entity.RefreshToken;
import com.dirijable.springstarter.financetracker.security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public void deleteByTokenAndUserId(String token, Long userId){
        refreshTokenRepository.deleteByTokenAndUserId(token, userId);
    }

    @Transactional
    public void deleteByToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }

    @Transactional
    public void deleteAllByUserId(Long userId){
        refreshTokenRepository.deleteAllByUserId(userId);
    }

    @Transactional
    public void deleteById(Long id){
        refreshTokenRepository.deleteById(id);
    }

    @Transactional
    public RefreshToken create(String email) {
        User userByEmail = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("user with email='%s' not found".formatted(email)));
        RefreshToken refreshToken = RefreshToken.builder()
                .token(generateRefreshToken())
                .expiryDate(Instant.now().plusMillis(432000000))
                .user(userByEmail)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired");
        }
        return token;
    }
}
