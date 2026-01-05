package com.dirijable.springstarter.financetracker.exception.business.conflict;

import com.dirijable.springstarter.financetracker.exception.base.BaseConflictException;

public class CategoryAlreadyExistException extends BaseConflictException {
    public CategoryAlreadyExistException(String message) {
        super(message);
    }
}
