package com.dirijable.springstarter.financetracker.exception.business.conflict;

import com.dirijable.springstarter.financetracker.exception.base.BaseConflictException;

public class NotEnoughMoneyException extends BaseConflictException {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
