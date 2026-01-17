package com.dirijable.springstarter.financetracker.security.service;

import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.dto.user.UserCreateDto;
import com.dirijable.springstarter.financetracker.dto.user.UserResponseDto;
import com.dirijable.springstarter.financetracker.exception.business.conflict.EmailAlreadyExistException;
import com.dirijable.springstarter.financetracker.mapper.UserMapper;
import com.dirijable.springstarter.financetracker.repository.UserRepository;
import com.dirijable.springstarter.financetracker.security.dto.JwtResponse;
import com.dirijable.springstarter.financetracker.security.dto.LoginRequest;
import com.dirijable.springstarter.financetracker.security.dto.RefreshTokenRequest;
import com.dirijable.springstarter.financetracker.security.entity.RefreshToken;
import com.dirijable.springstarter.financetracker.security.jwt.JwtUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(
        level = AccessLevel.PRIVATE,
        makeFinal = true
)
public class AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    AuthenticationManager authenticationManager;
    RefreshTokenService refreshTokenService;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    JwtUtils jwtUtils;

    @Transactional
    public JwtResponse login(LoginRequest request) {
        final Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        final String accessToken = jwtUtils.generateToken(authenticate.getName());
        final RefreshToken refreshToken = refreshTokenService.create(authenticate.getName());
        return new JwtResponse(accessToken, refreshToken.getToken(), authenticate.getName());
    }

    @Transactional
    public void logoutFromOneDevice(RefreshTokenRequest  token, Long userId){
        refreshTokenService.deleteByTokenAndUserId(token.refreshToken(), userId);
    }

    @Transactional
    public void logoutFromAllDevices(Long userId){
        refreshTokenService.deleteAllByUserId(userId);
    }

    @Transactional
    public UserResponseDto registration(UserCreateDto userDto) {
        if (userRepository.existsUserByEmail(userDto.email()))
            throw new EmailAlreadyExistException("user with email='%s' already exist".formatted(userDto.email()));
        final String encodedPassword = bCryptPasswordEncoder.encode(userDto.password());
        final User toSave = userMapper.toEntity(userDto);
        toSave.setPassword(encodedPassword);
        final User user = userRepository.save(toSave);
        return userMapper.toResponse(user);
    }

    @Transactional
    public JwtResponse refreshAccessToken(RefreshTokenRequest token) {
        String refreshToken = token.refreshToken();
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(oldToken -> {
                    User user = oldToken.getUser();
                    refreshTokenService.deleteById(oldToken.getId());

                    String newAccessToken = jwtUtils.generateToken(user.getUsername());
                    String newRefreshToken = refreshTokenService.create(user.getUsername()).getToken();

                    return new JwtResponse(newAccessToken, newRefreshToken, user.getUsername());
                })
                .orElseThrow(() -> new RuntimeException("refresh token isn`t in database"));
    }
}
