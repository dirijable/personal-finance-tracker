package com.dirijable.springstarter.financetracker.security.service;

import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.dto.user.UserCreateDto;
import com.dirijable.springstarter.financetracker.dto.user.UserResponseDto;
import com.dirijable.springstarter.financetracker.exception.business.conflict.EmailAlreadyExistException;
import com.dirijable.springstarter.financetracker.mapper.UserMapper;
import com.dirijable.springstarter.financetracker.repository.UserRepository;
import com.dirijable.springstarter.financetracker.security.dto.JwtResponse;
import com.dirijable.springstarter.financetracker.security.dto.LoginRequest;
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
    BCryptPasswordEncoder bCryptPasswordEncoder;
    JwtUtils jwtUtils;

    public JwtResponse login(LoginRequest request){
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        String token = jwtUtils.generateToken(authenticate.getName());
        return new JwtResponse(token, authenticate.getName());
    }

    @Transactional
    public UserResponseDto registration(UserCreateDto userDto){
        if(userRepository.existsUserByEmail(userDto.email()))
            throw new EmailAlreadyExistException("user with email='%s' already exist".formatted(userDto.email()));
        String encodedPassword = bCryptPasswordEncoder.encode(userDto.password());
        User toSave = userMapper.toEntity(userDto);
        toSave.setPassword(encodedPassword);
        User user = userRepository.save(toSave);
        return userMapper.toResponse(user);
    }
}
