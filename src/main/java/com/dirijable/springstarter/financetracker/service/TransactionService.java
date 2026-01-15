package com.dirijable.springstarter.financetracker.service;

import com.dirijable.springstarter.financetracker.database.entity.*;
import com.dirijable.springstarter.financetracker.dto.transaction.TransactionCreateDto;
import com.dirijable.springstarter.financetracker.dto.transaction.TransactionResponseDto;
import com.dirijable.springstarter.financetracker.dto.transaction.TransactionUpdateDto;
import com.dirijable.springstarter.financetracker.exception.business.conflict.CurrencyNotEqualsException;
import com.dirijable.springstarter.financetracker.exception.business.conflict.NotEnoughMoneyException;
import com.dirijable.springstarter.financetracker.exception.business.denied.AccessDeniedException;
import com.dirijable.springstarter.financetracker.exception.business.notfound.AccountNotFoundException;
import com.dirijable.springstarter.financetracker.exception.business.notfound.CategoryNotFoundException;
import com.dirijable.springstarter.financetracker.exception.business.notfound.TransactionNotFoundException;
import com.dirijable.springstarter.financetracker.exception.business.notfound.UserNotFoundException;
import com.dirijable.springstarter.financetracker.mapper.TransactionMapper;
import com.dirijable.springstarter.financetracker.repository.AccountRepository;
import com.dirijable.springstarter.financetracker.repository.CategoryRepository;
import com.dirijable.springstarter.financetracker.repository.TransactionRepository;
import com.dirijable.springstarter.financetracker.repository.UserRepository;
import com.dirijable.springstarter.financetracker.security.service.SecurityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(
        level = AccessLevel.PRIVATE,
        makeFinal = true
)
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    TransactionRepository transactionRepository;
    CategoryRepository categoryRepository;
    AccountRepository accountRepository;
    TransactionMapper transactionMapper;

    @PreAuthorize("#userId == authentication.principal.id")
    public List<TransactionResponseDto> findAllByUserId(Long userId) {
        return transactionRepository.findAllByAccountUserId(userId)
                .stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    @PreAuthorize("@securityService.canAccessTransaction(#transactionId)")
    public TransactionResponseDto findById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .map(transactionMapper::toResponse)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId.toString()));
    }

    @PreAuthorize("@securityService.canAccessTransaction(#transactionId)")
    @Transactional
    public void deleteById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("transaction with id='%d' not found".formatted(transactionId)));
        Account account = transaction.getAccount();
        account.reverseTransaction(transaction.getAmount(), transaction.getTransactionType());
        transactionRepository.delete(transaction);
    }

    @PreAuthorize("""
            @securityService.canAccessAccount(#dto.accountId()) &&
            @securityService.canAccessCategory(#dto.categoryId())
            """)
    @Transactional
    public TransactionResponseDto create(TransactionCreateDto dto) {
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(dto.categoryId().toString()));
        Account account = accountRepository.findById(dto.accountId())
                .orElseThrow(() -> new AccountNotFoundException(dto.accountId().toString()));
        if (dto.transactionType().equals(TransactionType.EXPENSE) && account.getBalance().compareTo(dto.amount()) < 0)
            throw new NotEnoughMoneyException("account with id='%d' haven`t enough money".formatted(dto.accountId()));
        account.updateBalance(dto.amount(), dto.transactionType());
        Transaction entity = transactionMapper.toEntity(dto);
        if (dto.transactionDate() == null)
            entity.setTransactionDate(Instant.now());
        entity.setAccount(account);
        entity.setCategory(category);
        Transaction savedTransaction = transactionRepository.save(entity);
        return transactionMapper.toResponse(savedTransaction);
    }

    @PreAuthorize("""
            @securityService.canAccessTransaction(#transactionId) &&
            (#updateDto.accountId() == null || @securityService.canAccessAccount(#updateDto.accountId()))
            """)
    @Transactional
    public TransactionResponseDto updateById(TransactionUpdateDto updateDto, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("transaction with id='%d' not found".formatted(transactionId)));
        processFinancialChanges(transaction, updateDto);
        transactionMapper.updateEntity(updateDto, transaction);
        return transactionMapper.toResponse(transaction);
    }

    private void processFinancialChanges(Transaction dbEntity, TransactionUpdateDto updateDto) {
        Account newAccount = updateDto.accountId() == null
                ? dbEntity.getAccount()
                : accountRepository.findById(updateDto.accountId())
                .orElseThrow(() -> new AccountNotFoundException("account with id='%d' not found".formatted(updateDto.accountId())));
        if (!dbEntity.getAccount().getCurrency().equals(newAccount.getCurrency())) {
            throw new CurrencyNotEqualsException("Expected currency='%s' but found '%s'".formatted(newAccount.getCurrency(), dbEntity.getAccount().getCurrency()));
        }

        dbEntity.getAccount().reverseTransaction(dbEntity.getAmount(), dbEntity.getTransactionType());
        TransactionType targetType = updateDto.transactionType() == null
                ? dbEntity.getTransactionType()
                : updateDto.transactionType();
        BigDecimal targetAmount = updateDto.amount() == null
                ? dbEntity.getAmount()
                : updateDto.amount();

        if (targetType == TransactionType.EXPENSE && newAccount.getBalance().compareTo(targetAmount) < 0) {
            throw new NotEnoughMoneyException("There are not enough money on account with id='%d'".formatted(newAccount.getId()));
        }
        newAccount.updateBalance(targetAmount, targetType);
        dbEntity.setAccount(newAccount);
    }
}
