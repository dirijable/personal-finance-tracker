package com.dirijable.springstarter.financetracker.security.dto;

public record JwtResponse(
        String accessToken,
        String email
) {}
