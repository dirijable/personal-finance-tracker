package com.dirijable.springstarter.financetracker.http.rest;

import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.dto.account.AccountCreateDto;
import com.dirijable.springstarter.financetracker.dto.account.AccountResponseDto;
import com.dirijable.springstarter.financetracker.dto.account.AccountUpdateDto;
import com.dirijable.springstarter.financetracker.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountRestController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> findAllByUserId(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(accountService.findAllByUserId(user.getId()));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> findById(@PathVariable("accountId") Long accountId,
                                                       @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(accountService.findById(accountId, user.getId()));
    }

    @PostMapping
    public ResponseEntity<AccountResponseDto> create(@RequestBody @Validated AccountCreateDto createDto,
                                                     @AuthenticationPrincipal User user) {
        AccountResponseDto responseDto = accountService.create(createDto, user.getId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{accountId}")
                .buildAndExpand(responseDto.id())
                .toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @PatchMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> updateById(@PathVariable("accountId") Long accountId,
                                                         @RequestBody AccountUpdateDto updateDto) {
        AccountResponseDto responseDto = accountService.updateById(updateDto, accountId);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteById(@PathVariable("accountId") Long accountId) {

        accountService.deleteById(accountId);
        return ResponseEntity.noContent().build();
    }


}
