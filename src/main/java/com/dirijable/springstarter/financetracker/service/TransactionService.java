package com.dirijable.springstarter.financetracker.service;

import com.dirijable.springstarter.financetracker.database.entity.Account;
import com.dirijable.springstarter.financetracker.database.entity.Category;
import com.dirijable.springstarter.financetracker.database.entity.Transaction;
import com.dirijable.springstarter.financetracker.database.entity.TransactionType;
import com.dirijable.springstarter.financetracker.dto.transaction.TransactionCreateDto;
import com.dirijable.springstarter.financetracker.dto.transaction.TransactionResponseDto;
import com.dirijable.springstarter.financetracker.dto.transaction.TransactionUpdateDto;
import com.dirijable.springstarter.financetracker.mapper.TransactionMapper;
import com.dirijable.springstarter.financetracker.repository.AccountRepository;
import com.dirijable.springstarter.financetracker.repository.CategoryRepository;
import com.dirijable.springstarter.financetracker.repository.TransactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@FieldDefaults(
        level = AccessLevel.PRIVATE,
        makeFinal = true
)
@RequiredArgsConstructor
public class TransactionService {

    TransactionRepository transactionRepository;
    CategoryRepository categoryRepository;
    AccountRepository accountRepository;
    TransactionMapper transactionMapper;


    public TransactionResponseDto create(TransactionCreateDto dto) {
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("category with id='%d' not found".formatted(dto.categoryId())));
        Account account = accountRepository.findById(dto.accountId())
                .orElseThrow(() -> new IllegalArgumentException("account with id='%d' not found".formatted(dto.accountId())));
        account.updateBalance(dto.amount(), dto.transactionType());
        Transaction entity = transactionMapper.toEntity(dto);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setAccount(account);
        entity.setCategory(category);
        Transaction savedTransaction = transactionRepository.save(entity);
        return transactionMapper.toResponse(savedTransaction);
    }


// TODO сделать проверку смены акка. по сути если он меняется, проверить валюту и хватит ли баланса в случае трат
// TODO если менялся баланс доедлать изменение баланса в зависимости от типа транзакции
    public TransactionResponseDto updateById(TransactionUpdateDto updateDto, Long transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("transaction with id='%d' not found".formatted(transactionId)));
        Account maybeUpdateAccount = accountRepository.findById(updateDto.accountId()).get();
        if (transaction.getAccount() != null && !transaction.getAccount().equals(maybeUpdateAccount))
            handleChangeAccount(transaction, updateDto);

        if (transaction.getAmount() != null && !transaction.getAmount().equals(updateDto.amount()))
            handleChangeAmount(transaction, updateDto);
        return null;
    }

    private void handleChangeAccount(Transaction transaction, TransactionUpdateDto updateDto) {

    }

    private void handleChangeAmount(Transaction dbEntity, TransactionUpdateDto updateDto) {

//        Account account = accountRepository.findById(updateDto.accountId())
//                .orElseThrow(() -> new IllegalArgumentException("account with id='%d' not found".formatted(updateDto.accountId())));
//        if (updateDto.transactionType().equals(TransactionType.EXPENSE) && account.getBalance().compareTo(updateDto.amount()) < 0)
//            throw new IllegalArgumentException("account balance '%.2f' but update balance for EXPENSE is '%.2f'".formatted(
//                    account.getBalance().setScale(2, RoundingMode.HALF_UP),
//                    updateDto.amount().setScale(2, RoundingMode.HALF_UP)));
//
//        TransactionType transactionType = updateDto.transactionType() == null ? dbEntity.getTransactionType() : updateDto.transactionType();
//        account.updateBalance(updateDto.amount(), transactionType);
    }

    public TransactionResponseDto findById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("transaction with id='%d' not found".formatted(transactionId)));
        return transactionMapper.toResponse(transaction);
    }
}
