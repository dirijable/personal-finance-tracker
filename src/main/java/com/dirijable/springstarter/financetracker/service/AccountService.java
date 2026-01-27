package com.dirijable.springstarter.financetracker.service;

import com.dirijable.springstarter.financetracker.database.entity.Account;
import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.database.pagination.PageResponse;
import com.dirijable.springstarter.financetracker.dto.account.AccountCreateDto;
import com.dirijable.springstarter.financetracker.dto.account.AccountResponseDto;
import com.dirijable.springstarter.financetracker.dto.account.AccountUpdateDto;
import com.dirijable.springstarter.financetracker.exception.business.conflict.AccountAlreadyExistsException;
import com.dirijable.springstarter.financetracker.exception.business.notfound.AccountNotFoundException;
import com.dirijable.springstarter.financetracker.exception.business.notfound.UserNotFoundException;
import com.dirijable.springstarter.financetracker.mapper.AccountMapper;
import com.dirijable.springstarter.financetracker.repository.AccountRepository;
import com.dirijable.springstarter.financetracker.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(
        makeFinal = true,
        level = AccessLevel.PRIVATE
)
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    AccountRepository accountRepository;
    UserRepository userRepository;
    AccountMapper accountMapper;

    @PreAuthorize("#userId == authentication.principal.id")
    public PageResponse<AccountResponseDto> findAllByUserId(Long userId, Pageable pageable) {
        return PageResponse.of(
                accountRepository.findAllByUserId(userId, pageable)
                .map(accountMapper::toResponse)
        );
    }

    @PreAuthorize("#userId == authentication.principal.id")
    public AccountResponseDto findById(Long accountId, Long userId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new UserNotFoundException("account with id='%d' not found".formatted(accountId)));
        if (!account.getUser().getId().equals(userId)) {
            throw new AccountNotFoundException("User with id='%d' have not account with id='%d'".formatted(userId, accountId));
        }
        return accountMapper.toResponse(account);
    }

    @PreAuthorize("#userId == authentication.principal.id")
    @Transactional
    public AccountResponseDto create(AccountCreateDto createDto, Long userId) {
        Account account = accountMapper.toEntity(createDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user with id='%d' not found".formatted(userId)));
        if (accountRepository.existsByNameAndUserId(createDto.name(), user.getId()))
            throw new AccountAlreadyExistsException("user with id='%d' already has account with name='%s'".formatted(user.getId(), createDto.name()));
        user.addAccount(account);
        accountRepository.save(account);
        return accountMapper.toResponse(account);
    }

    @PreAuthorize("@securityService.canAccessAccount(#accountId)")
    @Transactional
    public AccountResponseDto updateById(AccountUpdateDto updateDto, Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId.toString()));
        accountMapper.updateEntity(updateDto, account);
        return accountMapper.toResponse(account);
    }

    @PreAuthorize("@securityService.canAccessAccount(#accountId)")
    @Transactional
    public void deleteById(Long accountId) {
        accountRepository.deleteById(accountId);
    }


}
