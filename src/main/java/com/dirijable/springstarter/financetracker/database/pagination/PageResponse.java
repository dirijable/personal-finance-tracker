package com.dirijable.springstarter.financetracker.database.pagination;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        Metadata metadata
) {
    public static <T> PageResponse<T> of(Page<T> page) {
        Metadata metadata = new Metadata(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
        return new PageResponse<>(page.getContent(), metadata);
    }

    public record Metadata(
            int page,
            int size,
            long totalElements,
            int totalPages
    ) {
    }
}