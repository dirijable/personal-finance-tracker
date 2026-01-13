package com.dirijable.springstarter.financetracker.exception.base;

import org.springframework.http.HttpStatus;

public class BaseAuthorizationException extends FinanceTrackerException{
    public BaseAuthorizationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
