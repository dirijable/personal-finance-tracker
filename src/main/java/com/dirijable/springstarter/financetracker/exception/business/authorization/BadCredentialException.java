package com.dirijable.springstarter.financetracker.exception.business.authorization;

import com.dirijable.springstarter.financetracker.exception.base.BaseAuthorizationException;

public class BadCredentialException extends BaseAuthorizationException {
    public BadCredentialException(String message) {
        super(message);
    }
}
