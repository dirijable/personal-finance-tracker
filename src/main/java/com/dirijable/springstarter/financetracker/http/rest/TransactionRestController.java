package com.dirijable.springstarter.financetracker.http.rest;

import com.dirijable.springstarter.financetracker.dto.transaction.TransactionCreateDto;
import com.dirijable.springstarter.financetracker.dto.transaction.TransactionResponseDto;
import com.dirijable.springstarter.financetracker.dto.transaction.TransactionUpdateDto;
import com.dirijable.springstarter.financetracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/transactions")
@RequiredArgsConstructor
public class TransactionRestController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> findAllByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(transactionService.findAllByUserId(userId));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> findByUserId(@PathVariable("transactionId") Long transactionId,
                                                               @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(transactionService.findById(transactionId, userId));
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDto> create(@PathVariable("userId") Long userId,
                                                         @RequestBody @Validated TransactionCreateDto createDto) {
        TransactionResponseDto responseDto = transactionService.create(createDto, userId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{transactionId}")
                .buildAndExpand(responseDto.id())
                .toUri();
        return ResponseEntity.created(uri)
                .body(responseDto);
    }

    @PatchMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> updateById(@PathVariable("transactionId") Long transactionId,
                                                             @PathVariable("userId") Long userId,
                                                             @RequestBody @Validated TransactionUpdateDto updateDto) {
        TransactionResponseDto responseDto = transactionService.updateById(updateDto, transactionId, userId);
        return ResponseEntity.ok(responseDto);

    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteById(@PathVariable("transactionId") Long transactionId,
                                           @PathVariable("userId") Long userId) {
        transactionService.deleteById(transactionId, userId);
        return ResponseEntity.noContent().build();
    }

}
