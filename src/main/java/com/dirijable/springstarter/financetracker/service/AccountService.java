package com.dirijable.springstarter.financetracker.service;

import com.dirijable.springstarter.financetracker.database.entity.Account;
import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.dto.account.AccountCreateDto;
import com.dirijable.springstarter.financetracker.dto.account.AccountResponseDto;
import com.dirijable.springstarter.financetracker.dto.account.AccountUpdateDto;
import com.dirijable.springstarter.financetracker.exception.business.denied.AccessDeniedException;
import com.dirijable.springstarter.financetracker.exception.business.notfound.AccountNotFoundException;
import com.dirijable.springstarter.financetracker.exception.business.notfound.UserNotFoundException;
import com.dirijable.springstarter.financetracker.mapper.AccountMapper;
import com.dirijable.springstarter.financetracker.repository.AccountRepository;
import com.dirijable.springstarter.financetracker.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(
        makeFinal = true,
        level = AccessLevel.PRIVATE
)
@RequiredArgsConstructor
public class AccountService {

    AccountRepository accountRepository;
    UserRepository userRepository;
    AccountMapper accountMapper;

    public List<AccountResponseDto> findAllByUserId(Long userId) {

        if(!userRepository.existsById(userId))
            throw new UserNotFoundException("user with id='%d' not found".formatted(userId));
        return accountRepository.findAllByUserId(userId)
                .stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    public AccountResponseDto findById(Long accountId, Long userId){
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new UserNotFoundException("account with id='%d' not found".formatted(accountId)));
        if(!account.getUser().getId().equals(userId)){
            throw new AccountNotFoundException("User with id='%d' have not account with id='%d'".formatted(userId, accountId));
        }
        return accountMapper.toResponse(account);
    }

    public AccountResponseDto create(AccountCreateDto createDto, Long userId){
        Account account = accountMapper.toEntity(createDto);
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException("user with id='%d' not found".formatted(userId)));
        user.addAccount(account);
        accountRepository.save(account);
        return accountMapper.toResponse(account);
    }

    public AccountResponseDto updateById(AccountUpdateDto updateDto, Long accountId, Long userId){

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("account with id='%d' not found".formatted(accountId)));
        if(!account.getUser().getId().equals(userId)){
            throw new AccessDeniedException("User with id='%d' have not account with id='%d'".formatted(userId, accountId));
        }
        accountMapper.updateEntity(updateDto, account);
        return accountMapper.toResponse(account);
    }

    public void deleteById(Long accountId, Long userId){
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new UserNotFoundException("account with id='%d' not found".formatted(accountId)));
        if(!account.getUser().getId().equals(userId)){
            throw new AccountNotFoundException("User with id='%d' have not account with id='%d'".formatted(userId, accountId));
        }
        accountRepository.deleteById(accountId);
    }


}
