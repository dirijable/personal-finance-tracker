package com.dirijable.springstarter.financetracker.http.rest;

import com.dirijable.springstarter.financetracker.dto.account.AccountCreateDto;
import com.dirijable.springstarter.financetracker.dto.account.AccountResponseDto;
import com.dirijable.springstarter.financetracker.dto.account.AccountUpdateDto;
import com.dirijable.springstarter.financetracker.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/accounts")
@RequiredArgsConstructor
public class AccountRestController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> findAllByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(accountService.findAllByUserId(userId));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> findById(@PathVariable("accountId") Long accountId,
                                                       @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(accountService.findById(accountId, userId));
    }

    @PostMapping
    public ResponseEntity<AccountResponseDto> create(@RequestBody @Validated AccountCreateDto createDto,
                                                     @PathVariable("userId") Long userId) {
        AccountResponseDto responseDto = accountService.create(createDto, userId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{accountId}")
                .buildAndExpand(responseDto.id())
                .toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @PatchMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> updateById(@PathVariable("accountId") Long accountId,
                                                         @PathVariable("userId") Long userId,
                                                         @RequestBody AccountUpdateDto updateDto) {
        AccountResponseDto responseDto = accountService.updateById(updateDto, accountId, userId);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteById(@PathVariable("accountId") Long accountId,
                                           @PathVariable("userId") Long userId) {

        accountService.deleteById(accountId, userId);
        return ResponseEntity.noContent().build();
    }


}
