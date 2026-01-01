package com.dirijable.springstarter.financetracker.service;

import com.dirijable.springstarter.financetracker.database.entity.Account;
import com.dirijable.springstarter.financetracker.database.entity.Currency;
import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.dto.account.AccountCreateDto;
import com.dirijable.springstarter.financetracker.dto.account.AccountResponseDto;
import com.dirijable.springstarter.financetracker.dto.account.AccountUpdateDto;
import com.dirijable.springstarter.financetracker.mapper.AccountMapper;
import com.dirijable.springstarter.financetracker.repository.AccountRepository;
import com.dirijable.springstarter.financetracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;
    private final AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
    private AccountService accountService;

    @BeforeEach
    void initService() {
        accountService = new AccountService(accountRepository, userRepository, accountMapper);
    }

    @Nested
    class DeleteById {

        @Test
        void deleteById_Success() {
            Long existingUserId = 200L;
            Long existingAccountId = 200L;
            Account account = Account.builder()
                    .user(getUserWithYourId(existingUserId))
                    .build();
            when(accountRepository.findById(existingAccountId)).thenReturn(Optional.of(account));
            accountService.deleteById(existingAccountId, existingUserId);
            verify(accountRepository, times(1)).deleteById(existingAccountId);
        }

        @Test
        void deleteById_AccountNotFound_ThrowsException() {
            Long accountId = 1L;
            when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () -> accountService.deleteById(accountId, 5L));

            assertThatThrownBy(() -> accountService.deleteById(accountId, 5L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("account with id='%d' not found".formatted(accountId));
            verify(accountRepository, never()).deleteById(anyLong());

        }

        @Test
        void deleteById_UserIsNotOwner_ThrowsException() {

            Long accountId = 1L;
            Long ownerId = 10L;
            Long notOwnerUserId = 99L;

            User owner = User.builder().id(ownerId).build();
            Account account = Account.builder().id(accountId).user(owner).build();

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

            assertThrows(IllegalArgumentException.class, () -> accountService.deleteById(accountId, notOwnerUserId));
            assertThatThrownBy(() -> accountService.deleteById(accountId, notOwnerUserId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("User with id='%d' have not account with id='%d'".formatted(notOwnerUserId, accountId));

            verify(accountRepository, never()).deleteById(accountId);

        }
    }
    @Nested
    class UpdateById {

        @Test
        void shouldThrown_WhenUserNotOwner() {
            AccountUpdateDto dummyDto = new AccountUpdateDto("dummyName", "dummyDescription");
            Long notOwnerUserId = 404L;
            Long ownerUserId = 200L;
            Long existingAccountId = 200L;
            Account account = Account.builder()
                    .user(getUserWithYourId(ownerUserId))
                    .build();
            when(accountRepository.findById(existingAccountId)).thenReturn(Optional.of(account));
            assertThatThrownBy(() -> accountService.updateById(dummyDto, existingAccountId, notOwnerUserId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("User with id='%d' have not account with id='%d'".formatted(notOwnerUserId, existingAccountId));

        }

        @Test
        void shouldThrow_WhenAccountNotFound() {
            AccountUpdateDto dummyDto = new AccountUpdateDto("dummyName", "dummyDescription");
            Long notExistingAccountId = 404L;
            Long dummyUserId = 200L;
            when(accountRepository.findById(notExistingAccountId)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> accountService.updateById(dummyDto, notExistingAccountId, dummyUserId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("account with id='%d' not found".formatted(notExistingAccountId));
        }

        @Test
        void update_ShouldReturnUpdatedEntity() {
            Long existingAccountId = 1L;
            Long existingUserId = 1L;
            User existingUser = getUserWithYourId(existingUserId);
            Account account = Account.builder()
                    .name("old name")
                    .description("old description")
                    .user(existingUser)
                    .build();
            AccountUpdateDto updateDto = new AccountUpdateDto("updated name", "updated description");
            when(accountRepository.findById(existingAccountId)).thenReturn(Optional.of(account));
            AccountResponseDto responseDto = accountService.updateById(updateDto, existingAccountId, existingUserId);

            assertThat(account.getName()).isEqualTo("updated name");
            assertThat(account.getDescription()).isEqualTo("updated description");

            assertThat(responseDto.name()).isEqualTo("updated name");
            assertThat(responseDto.description()).isEqualTo("updated description");


        }
    }

    @Nested
    class Create {

        @Test
        void create_ShouldReturnDtoAndLinkToUser_When_UserExists() {
            AccountCreateDto createDto = new AccountCreateDto(
                    "alpha-bank",
                    null,
                    new BigDecimal(100),
                    Currency.BYN
            );
            User user = getUserWithYourId(1L);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            AccountResponseDto accountResponseDto = accountService.create(createDto, 1L);
            AccountResponseDto expectedResponse = new AccountResponseDto(
                    null,
                    "alpha-bank",
                    null,
                    new BigDecimal(100),
                    Currency.BYN
            );
            assertThat(accountResponseDto).isEqualTo(expectedResponse);
            assertThat(user.getAccounts()).hasSize(1);
            Account createdAccount = user.getAccounts().getFirst();
            assertThat(createdAccount.getName()).isEqualTo("alpha-bank");
            assertThat(createdAccount.getUser()).isEqualTo(user);
        }

        @Test
        void shouldThrowException_When_UserNotExists() {
            Long notExistingUserId = 200L;
            when(userRepository.findById(notExistingUserId)).thenReturn(Optional.empty());
            assertThrows(
                    IllegalArgumentException.class,
                    () -> accountService.create(null, notExistingUserId)
            );
        }


    }

    @Nested
    class FindById {
        @Test
        void checkResponseDataEqualsEntityData() {
            User user = getUserWithYourId(1L);
            Account account = Account.builder()
                    .id(1L)
                    .name("alpha-bank")
                    .user(user)
                    .balance(new BigDecimal(1500))
                    .currency(Currency.BYN)
                    .build();
            when(accountRepository.findById(1L))
                    .thenReturn(Optional.of(account));
            AccountResponseDto findResult = accountService.findById(1L, 1L);
            assertThat(findResult).isNotNull();
            assertThat(findResult.id()).isEqualTo(account.getId());
            assertThat(findResult.name()).isEqualTo(account.getName());
            assertThat(findResult.description()).isEqualTo(account.getDescription());
            assertThat(findResult.balance()).isEqualTo(account.getBalance());
            assertThat(findResult.currency()).isEqualTo(account.getCurrency());
        }

        @Test
        void checkThrowExceptionWhenUserIsNotOwner() {
            Long accountId = 1L;
            Long realOwnerId = 2L;
            Long badOwnerId = 3L;

            User realOwner = User.builder()
                    .id(realOwnerId)
                    .build();
            Account account = Account.builder()
                    .id(accountId)
                    .user(realOwner)
                    .build();
            when(accountRepository.findById(accountId))
                    .thenReturn(Optional.of(account));
            assertThrows(
                    IllegalArgumentException.class,
                    () -> accountService.findById(accountId, badOwnerId)
            );
        }

        @Test
        void checkThrowExceptionWhenAccountNotFound() {
            Long notExistingAccountId = 1L;
            Long dummyUserId = 1L;
            when(accountRepository.findById(notExistingAccountId))
                    .thenReturn(Optional.empty());
            assertThrows(
                    IllegalArgumentException.class,
                    () -> accountService.findById(notExistingAccountId, dummyUserId)
            );
        }


    }

    private User getUserWithYourId(Long id) {
        return User.builder()
                .id(id)
                .build();
    }
}


