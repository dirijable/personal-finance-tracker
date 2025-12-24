package com.dirijable.springstarter.financetracker.service;

import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.dto.UserUpdateDto;
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

    public User findById(Long userId) {
        var maybeUser = userRepository.findById(userId);
        return maybeUser.orElse(null);
    }

    public void updateById(Long userId, UserUpdateDto dto) {
        if (dto.email() == null && dto.username() == null)
            throw new IllegalArgumentException("email and username == null");

        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        String oldEmail = user.getEmail();
        String oldUsername = user.getUsername();
        if (dto.email() != null) {
            if(userRepository.existsUserByEmail(dto.email()))
                throw new IllegalArgumentException("email already exist");
            user.setEmail(dto.email());
        }
        if (dto.username() != null)
            user.setUsername(dto.username());
        log.info(
                "User id={} updated. Old email={}, new email={}. Old username={}, new username={}",
                user.getId(), oldEmail, user.getEmail(),
                oldUsername, user.getUsername()
        );

    }

}
