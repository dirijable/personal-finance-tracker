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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
    UserRepository userRepository;
    TransactionMapper transactionMapper;

    public List<TransactionResponseDto> findAllByUserId(Long userId) {
        if (!userRepository.existsById(userId))
            throw new UserNotFoundException("user with id='%d' not found".formatted(userId));
        return transactionRepository.findAllByAccountUserId(userId)
                .stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    public TransactionResponseDto findById(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("transaction with id='%d' not found".formatted(transactionId)));
        if (!transaction.getAccount().getUser().getId().equals(userId))
            throw new AccessDeniedException("user with id='%d' haven`t transaction with id='%d' on account with id='%d'");
        return transactionMapper.toResponse(transaction);
    }

    @Transactional
    public void deleteById(Long transactionId, Long userId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("transaction with id='%d' not found".formatted(transactionId)));
        Account account = transaction.getAccount();
        if (!account.getUser().getId().equals(userId))
            throw new AccessDeniedException("User with id='%d' don`t have account with id='%d'".formatted(userId, transaction.getAccount().getId()));
        account.reverseTransaction(transaction.getAmount(), transaction.getTransactionType());
        transactionRepository.delete(transaction);
    }


    @Transactional
    public TransactionResponseDto create(TransactionCreateDto dto, Long userId) {
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("category with id='%d' not found".formatted(dto.categoryId())));
        Account account = accountRepository.findById(dto.accountId())
                .orElseThrow(() -> new AccountNotFoundException("account with id='%d' not found".formatted(dto.accountId())));

        if (!account.getUser().getId().equals(userId))
            throw new AccessDeniedException("User with id='%d' don`t have account with id='%d'".formatted(userId, dto.accountId()));
        if (!account.getUser().getCategories().contains(category))
            throw new AccessDeniedException("User with id='%d' don`t have category with id='%d'".formatted(userId, dto.categoryId()));
        if (dto.transactionType().equals(TransactionType.EXPENSE) && account.getBalance().compareTo(dto.amount()) < 0)
            throw new NotEnoughMoneyException("account with id='%d' haven`t enough money".formatted(dto.accountId()));

        account.updateBalance(dto.amount(), dto.transactionType());

        Transaction entity = transactionMapper.toEntity(dto);
        entity.setTransactionDate(Instant.now());
        entity.setAccount(account);
        entity.setCategory(category);
        Transaction savedTransaction = transactionRepository.save(entity);
        return transactionMapper.toResponse(savedTransaction);
    }

    @Transactional
    public TransactionResponseDto updateById(TransactionUpdateDto updateDto, Long transactionId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user with id='%d' not found".formatted(userId)));
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("transaction with id='%d' not found".formatted(transactionId)));
        if (!transaction.getAccount().getUser().getId().equals(userId))
            throw new AccessDeniedException("user with id='%d' haven`t transaction with id='%d' on account with id='%d'");
        processFinancialChanges(transaction, updateDto, user.getId());
        transactionMapper.updateEntity(updateDto, transaction);
        return transactionMapper.toResponse(transaction);
    }

    private void processFinancialChanges(Transaction dbEntity, TransactionUpdateDto updateDto, Long userId) {
        Account newAccount = updateDto.accountId() == null
                ? dbEntity.getAccount()
                : accountRepository.findById(updateDto.accountId())
                .orElseThrow(() -> new AccountNotFoundException("account with id='%d' not found".formatted(updateDto.accountId())));
        if (!newAccount.getUser().getId().equals(userId))
            throw new AccessDeniedException("user with id='%d' haven`t account with id='%d'".formatted(userId, newAccount.getId()));

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

        if (targetType == TransactionType.EXPENSE) {
            if (newAccount.getBalance().compareTo(targetAmount) < 0) {
                throw new NotEnoughMoneyException("There are not enough money on account with id='%d'".formatted(newAccount.getId()));
            }
        }
        newAccount.updateBalance(targetAmount, targetType);
        dbEntity.setAccount(newAccount);
    }
}
