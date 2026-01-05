package com.dirijable.springstarter.financetracker.exception.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class FinanceTrackerException extends RuntimeException {

    private final HttpStatus status;

    public FinanceTrackerException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
