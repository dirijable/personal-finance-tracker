package com.dirijable.springstarter.financetracker.http.rest;

import com.dirijable.springstarter.financetracker.dto.user.UserResponseDto;
import com.dirijable.springstarter.financetracker.dto.user.UserUpdateDto;
import com.dirijable.springstarter.financetracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateById(@PathVariable("userId") Long userId,
                                                  @RequestBody @Validated UserUpdateDto updateDto) {
        return ResponseEntity.ok(userService.updateById(userId, updateDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable("userId") Long userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

}
