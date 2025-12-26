package com.dirijable.springstarter.financetracker.mapper;

public interface Mapper<FROM, TO> {
    TO map(FROM entity);
}
