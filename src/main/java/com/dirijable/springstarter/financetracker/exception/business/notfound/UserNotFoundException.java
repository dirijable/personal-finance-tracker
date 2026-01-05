package com.dirijable.springstarter.financetracker.exception.business.notfound;

import com.dirijable.springstarter.financetracker.exception.base.BaseNotFoundException;

public class UserNotFoundException extends BaseNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
