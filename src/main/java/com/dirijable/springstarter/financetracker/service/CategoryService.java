package com.dirijable.springstarter.financetracker.service;

import com.dirijable.springstarter.financetracker.database.entity.Category;
import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.repository.CategoryRepository;
import com.dirijable.springstarter.financetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;


    public Category addCategory(String categoryName, Long userId) {

        if (categoryRepository.existsCategoriesByNameAndUserId(categoryName, userId)) {
            log.warn("Unable to add category with name '{}'. This category already exists", categoryName);
            throw new IllegalArgumentException();
        }
        Category category = Category.builder()
                .name(categoryName)
                .build();
        User maybeUser = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Unable to find user with id='{}'", userId);
            return new RuntimeException();
        });
        maybeUser.addCategory(category);
        log.info("Category with name '{}' added to user with id='{}'", category.getName(), maybeUser.getId());
//        log.info("Category with name '{}' added to db", category.getName());
        return categoryRepository.save(category);

    }
}
