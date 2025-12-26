package com.dirijable.springstarter.financetracker.mapper;

import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.dto.user.UserCreateDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<UserCreateDto, User> {
    @Override
    public User map(UserCreateDto entity) {
        return User.builder()
                .email(entity.email())
                .username(entity.username())
                .password(entity.password())
                .build();
    }
}
