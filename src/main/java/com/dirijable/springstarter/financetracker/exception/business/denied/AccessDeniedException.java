package com.dirijable.springstarter.financetracker.exception.business.denied;

import com.dirijable.springstarter.financetracker.exception.base.BaseForbiddenException;

public class AccessDeniedException extends BaseForbiddenException{
    public AccessDeniedException(String message) {
        super(message);
    }
}
