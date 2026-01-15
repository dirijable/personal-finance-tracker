package com.dirijable.springstarter.financetracker.security.service;

import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.exception.business.denied.AccessDeniedException;
import com.dirijable.springstarter.financetracker.exception.business.notfound.AccountNotFoundException;
import com.dirijable.springstarter.financetracker.exception.business.notfound.CategoryNotFoundException;
import com.dirijable.springstarter.financetracker.exception.business.notfound.TransactionNotFoundException;
import com.dirijable.springstarter.financetracker.repository.AccountRepository;
import com.dirijable.springstarter.financetracker.repository.CategoryRepository;
import com.dirijable.springstarter.financetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SecurityService {

    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public boolean canAccessTransaction(Long transactionId) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new TransactionNotFoundException("transaction with id='%d' not found".formatted(transactionId));
        }
        if (!transactionRepository.existsTransactionByIdAndUserId(transactionId, getUserId())) {
            throw new AccessDeniedException("You don't have permission to transaction with id='%d'".formatted(transactionId));
        }
        return true;
    }

    public boolean canAccessAccount(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException("account with id='%d' not found".formatted(accountId));
        }
        if (accountRepository.existsAccountByIdAndUserId(accountId, getUserId())) {
            throw new AccessDeniedException("You don't have permission to account with id='%d'".formatted(accountId));
        }
        return true;
    }

    public boolean canAccessCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("category with id='%d' not found".formatted(categoryId));
        }
        if (categoryRepository.existsCategoryByIdAndUserId(categoryId, getUserId())) {
            throw new AccessDeniedException("You don't have permission to category with id='%d'".formatted(categoryId));
        }
        return true;
    }



    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new AccessDeniedException("Access denied");
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user)
            return user.getId();
        throw new AccessDeniedException("principal not instance of User");
    }
}
