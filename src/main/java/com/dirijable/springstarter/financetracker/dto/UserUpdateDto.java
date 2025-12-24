package com.dirijable.springstarter.financetracker.dto;

import jakarta.validation.constraints.Email;

public record UserUpdateDto(
        String username,
        String password,
        @Email(message = "Email должен быть формата smth@gmail.com")
        String email
) {}
