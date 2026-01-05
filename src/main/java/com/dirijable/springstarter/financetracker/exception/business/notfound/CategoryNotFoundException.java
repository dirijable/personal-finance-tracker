package com.dirijable.springstarter.financetracker.exception.business.notfound;

import com.dirijable.springstarter.financetracker.exception.base.BaseNotFoundException;

public class CategoryNotFoundException extends BaseNotFoundException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
