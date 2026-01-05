package com.dirijable.springstarter.financetracker.exception.base;

import org.springframework.http.HttpStatus;

public abstract class BaseNotFoundException extends FinanceTrackerException {
    public BaseNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
