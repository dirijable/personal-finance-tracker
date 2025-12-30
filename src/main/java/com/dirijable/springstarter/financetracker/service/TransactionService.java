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

    public void deleteById(Long transactionId){
        if(!transactionRepository.existsById(transactionId))
            throw new IllegalArgumentException("transaction with id='%d' not found".formatted(transactionId));
        transactionRepository.deleteById(transactionId);
    }

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


    public TransactionResponseDto updateById(TransactionUpdateDto updateDto, Long transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("transaction with id='%d' not found".formatted(transactionId)));
        processFinancialChanges(transaction, updateDto);
        transactionMapper.updateEntity(updateDto, transaction);
        return transactionMapper.toResponse(transaction);
    }

    private void processFinancialChanges(Transaction dbEntity, TransactionUpdateDto updateDto) {
        Account newAccount = updateDto.accountId() == null
                ? dbEntity.getAccount()
                : accountRepository.findById(updateDto.accountId())
                .orElseThrow(() -> new IllegalArgumentException("account with id='%d' not found".formatted(updateDto.accountId())));

        if (!dbEntity.getAccount().getCurrency().equals(newAccount.getCurrency())) {
            throw new IllegalArgumentException("Expected currency='%s' but found '%s'".formatted(newAccount.getCurrency(), dbEntity.getAccount().getCurrency()));
        }

        dbEntity.getAccount().reverseTransaction(dbEntity.getAmount(), dbEntity.getTransactionType());
        TransactionType targetType = updateDto.transactionType() == null
                ? dbEntity.getTransactionType()
                : updateDto.transactionType();
        BigDecimal targetAmount = updateDto.amount() == null
                ? dbEntity.getAmount()
                : updateDto.amount();

        if (targetType == TransactionType.EXPENSE) {
            if (newAccount.getBalance().compareTo(targetAmount) < 0) {
                throw new IllegalArgumentException("There are not enough money on account with id='%d'".formatted(newAccount.getId()));
            }
        }
        newAccount.updateBalance(targetAmount, targetType);
        dbEntity.setAccount(newAccount);
    }


    public TransactionResponseDto findById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("transaction with id='%d' not found".formatted(transactionId)));
        return transactionMapper.toResponse(transaction);
    }
}
