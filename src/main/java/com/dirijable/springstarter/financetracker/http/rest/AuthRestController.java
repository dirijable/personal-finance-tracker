package com.dirijable.springstarter.financetracker.http.rest;

import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.dto.user.UserCreateDto;
import com.dirijable.springstarter.financetracker.dto.user.UserResponseDto;
import com.dirijable.springstarter.financetracker.security.dto.JwtResponse;
import com.dirijable.springstarter.financetracker.security.dto.LoginRequest;
import com.dirijable.springstarter.financetracker.security.dto.RefreshTokenRequest;
import com.dirijable.springstarter.financetracker.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutFromOneDevice(@RequestBody @Validated RefreshTokenRequest request,
                                                    @AuthenticationPrincipal User user) {
        authService.logoutFromOneDevice(request, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutFromAllDevices(@AuthenticationPrincipal User user) {
        authService.logoutFromAllDevices(user.getId());
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody @Validated RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshAccessToken(request));
    }

    @PostMapping("/registration")
    public ResponseEntity<UserResponseDto> registration(@RequestBody @Validated UserCreateDto createDto) {
        UserResponseDto responseDto = authService.registration(createDto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/api/v1/users/{userId}")
                .buildAndExpand(responseDto.id())
                .toUri();
        return ResponseEntity
                .created(uri)
                .body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Validated LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}
