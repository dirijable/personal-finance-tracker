package com.dirijable.springstarter.financetracker.exception.base;

import org.springframework.http.HttpStatus;

public class BaseConflictException extends FinanceTrackerException{
    public BaseConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
