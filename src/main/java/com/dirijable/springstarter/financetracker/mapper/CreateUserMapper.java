package com.dirijable.springstarter.financetracker.mapper;

import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.dto.CreateUserDto;
import org.springframework.stereotype.Component;

@Component
public class CreateUserMapper implements Mapper<CreateUserDto, User> {
    @Override
    public User map(CreateUserDto entity) {
        return User.builder()
                .email(entity.email())
                .username(entity.username())
                .password(entity.password())
                .build();
    }
}
