package com.dirijable.springstarter.financetracker.exception.business.conflict;

import com.dirijable.springstarter.financetracker.exception.base.BaseConflictException;

public class CurrencyNotEqualsException extends BaseConflictException {
    public CurrencyNotEqualsException(String message) {
        super(message);
    }
}
