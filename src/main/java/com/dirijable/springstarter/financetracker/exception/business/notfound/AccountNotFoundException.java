package com.dirijable.springstarter.financetracker.exception.business.notfound;

import com.dirijable.springstarter.financetracker.exception.base.BaseNotFoundException;

public class AccountNotFoundException extends BaseNotFoundException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
