package com.dirijable.springstarter.financetracker.exception.base;

import org.springframework.http.HttpStatus;

public class BaseForbiddenException extends FinanceTrackerException {
    public BaseForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
