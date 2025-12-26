package com.dirijable.springstarter.financetracker.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateDto(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank @Email String email
) {}
