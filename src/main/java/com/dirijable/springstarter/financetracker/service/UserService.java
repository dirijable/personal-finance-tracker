package com.dirijable.springstarter.financetracker.service;

import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.dto.user.UserResponseDto;
import com.dirijable.springstarter.financetracker.dto.user.UserUpdateDto;
import com.dirijable.springstarter.financetracker.exception.business.conflict.EmailAlreadyExistException;
import com.dirijable.springstarter.financetracker.exception.business.notfound.UserNotFoundException;
import com.dirijable.springstarter.financetracker.mapper.UserMapper;
import com.dirijable.springstarter.financetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponseDto findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user with id='%d' not found".formatted(userId)));
        return userMapper.toResponse(user);
    }

    @Transactional
    @PreAuthorize("#userId == authentication.principal.id")
    public UserResponseDto updateById(Long userId, UserUpdateDto dto) {
        if (dto.email() == null && dto.name() == null && dto.password() == null)
            throw new IllegalArgumentException("email, name and password == null");
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user with id='%d' not found".formatted(userId)));
        if (!Objects.equals(user.getEmail(), dto.email()) && userRepository.existsUserByEmail(dto.email()))
            throw new EmailAlreadyExistException("email already exist");
        userMapper.updateEntity(dto, user);
        if (dto.password() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(dto.password()));
        }
        log.info("User with id {} updated by owner", userId);
        return userMapper.toResponse(user);
    }

    @Transactional
    public void deleteById(Long userId) {
        if (!userRepository.existsById(userId))
            throw new UserNotFoundException("User with id='%d' not found".formatted(userId));
        userRepository.deleteById(userId);
    }
}
