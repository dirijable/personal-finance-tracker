package com.dirijable.springstarter.financetracker.exception.business.conflict;

import com.dirijable.springstarter.financetracker.exception.base.BaseConflictException;

public class EmailAlreadyExistException extends BaseConflictException {
    public EmailAlreadyExistException(String message) {
        super(message);
    }
}
