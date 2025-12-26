package com.dirijable.springstarter.financetracker.service;

import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.dto.CreateUserDto;
import com.dirijable.springstarter.financetracker.dto.UserUpdateDto;
import com.dirijable.springstarter.financetracker.mapper.Mapper;
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
    private final Mapper<CreateUserDto, User> createUserDtoUserMapper;

    public User findById(Long userId) {
        var maybeUser = userRepository.findById(userId);
        return maybeUser.orElse(null);
    }

    public void updateById(Long userId, UserUpdateDto dto) {
        if (dto.email() == null && dto.username() == null && dto.password() ==null)
            throw new IllegalArgumentException("email, username and password == null");

        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        String oldEmail = user.getEmail();
        String oldUsername = user.getUsername();
        if (dto.email() != null) {
            if (userRepository.existsUserByEmail(dto.email()))
                throw new IllegalArgumentException("email already exist");
            user.setEmail(dto.email());
        }
        if (dto.username() != null)
            user.setUsername(dto.username());
        if (dto.password() != null)
            user.setPassword(dto.password());
        log.info(
                "User id={} updated. Old email={}, new email={}. Old username={}, new username={}",
                user.getId(), oldEmail, user.getEmail(),
                oldUsername, user.getUsername()
        );
    }


    public boolean login(String email, String password){
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));
        return user.getPassword().equals(password);
    }


    public void register(CreateUserDto userDto){

        if(userRepository.existsUserByEmail(userDto.email()))
            throw new IllegalArgumentException("user with email '%s' already exist".formatted(userDto.email()));
        User user = createUserDtoUserMapper.map(userDto);
        userRepository.save(user);
    }

}
