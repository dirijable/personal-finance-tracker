package com.dirijable.springstarter.financetracker.exception.business.conflict;

import com.dirijable.springstarter.financetracker.exception.base.BaseConflictException;

public class AccountAlreadyExists extends BaseConflictException {
    public AccountAlreadyExists(String message) {
        super(message);
    }
}
