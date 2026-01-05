package com.dirijable.springstarter.financetracker.exception.business.notfound;

import com.dirijable.springstarter.financetracker.exception.base.BaseNotFoundException;

public class TransactionNotFoundException extends BaseNotFoundException {
    public TransactionNotFoundException(String message) {
        super(message);
    }
}
