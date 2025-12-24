package com.dirijable.springstarter.financetracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserDto(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank @Email String email
) {}
