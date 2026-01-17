package com.dirijable.springstarter.financetracker.exception.business.conflict;

import com.dirijable.springstarter.financetracker.exception.base.BaseConflictException;

public class AccountAlreadyExistsException extends BaseConflictException {
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
