package com.dirijable.springstarter.financetracker.service;

import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.dto.user.UserCreateDto;
import com.dirijable.springstarter.financetracker.dto.user.UserResponseDto;
import com.dirijable.springstarter.financetracker.dto.user.UserUpdateDto;
import com.dirijable.springstarter.financetracker.exception.business.conflict.EmailAlreadyExistException;
import com.dirijable.springstarter.financetracker.exception.business.notfound.UserNotFoundException;
import com.dirijable.springstarter.financetracker.mapper.UserMapper;
import com.dirijable.springstarter.financetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponseDto findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("user with id='%d' not found".formatted(userId)));
        return userMapper.toResponse(user);
    }

    public UserResponseDto updateById(Long userId, UserUpdateDto dto) {
        if (dto.email() == null && dto.username() == null && dto.password() ==null)
            throw new IllegalArgumentException("email, username and password == null");

        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("user with id='%d' not found".formatted(userId)));
        String oldEmail = user.getEmail();
        String oldUsername = user.getUsername();
            if (userRepository.existsUserByEmail(dto.email()))
                throw new EmailAlreadyExistException("email already exist");
        userMapper.updateEntity(dto, user);
        log.info(
                "User id={} updated. Old email={}, new email={}. Old username={}, new username={}",
                user.getId(), oldEmail, user.getEmail(),
                oldUsername, user.getUsername()
        );
        return userMapper.toResponse(user);
    }


    public boolean login(String email, String password){
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("user with email='%s' not found".formatted(email)));
        return user.getPassword().equals(password);
    }


    public UserResponseDto create(UserCreateDto userDto){
        if(userRepository.existsUserByEmail(userDto.email()))
            throw new EmailAlreadyExistException("user with email='%s' already exist".formatted(userDto.email()));
        User user = userRepository.save(userMapper.toEntity(userDto));
        return userMapper.toResponse(user);
    }

    public void deleteById(Long userId){
        if(!userRepository.existsById(userId))
            throw new UserNotFoundException("User with id='%d' not found".formatted(userId));
        userRepository.deleteById(userId);
    }


}
