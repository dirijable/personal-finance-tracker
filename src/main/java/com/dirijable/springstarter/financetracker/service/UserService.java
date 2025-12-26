package com.dirijable.springstarter.financetracker.service;

import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.dto.user.UserCreateDto;
import com.dirijable.springstarter.financetracker.dto.user.UserResponseDto;
import com.dirijable.springstarter.financetracker.dto.user.UserUpdateDto;
import com.dirijable.springstarter.financetracker.mapper.UserMapper;
import com.dirijable.springstarter.financetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDto findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("user with id='%d' not found".formatted(userId)));
        return userMapper.toResponse(user);
    }

    public UserResponseDto updateById(Long userId, UserUpdateDto dto) {
        if (dto.email() == null && dto.username() == null && dto.password() ==null)
            throw new IllegalArgumentException("email, username and password == null");

        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        String oldEmail = user.getEmail();
        String oldUsername = user.getUsername();
            if (userRepository.existsUserByEmail(dto.email()))
                throw new IllegalArgumentException("email already exist");
        userMapper.updateEntity(dto, user);
        log.info(
                "User id={} updated. Old email={}, new email={}. Old username={}, new username={}",
                user.getId(), oldEmail, user.getEmail(),
                oldUsername, user.getUsername()
        );
        return userMapper.toResponse(user);
    }


    public boolean login(String email, String password){
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));
        return user.getPassword().equals(password);
    }


    public UserResponseDto create(UserCreateDto userDto){
        if(userRepository.existsUserByEmail(userDto.email()))
            throw new IllegalArgumentException("user with email '%s' already exist".formatted(userDto.email()));
        User user = userRepository.save(userMapper.toEntity(userDto));
        return userMapper.toResponse(user);
    }

    public void deleteById(Long userId){
        if(!userRepository.existsById(userId))
            throw new IllegalArgumentException("User with id='%d' not found".formatted(userId));
        userRepository.deleteById(userId);
    }
}
