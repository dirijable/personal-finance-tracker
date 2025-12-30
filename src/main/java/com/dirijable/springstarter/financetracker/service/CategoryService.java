package com.dirijable.springstarter.financetracker.service;

import com.dirijable.springstarter.financetracker.database.entity.Category;
import com.dirijable.springstarter.financetracker.dto.category.CategoryCreateDto;
import com.dirijable.springstarter.financetracker.dto.category.CategoryResponseDto;
import com.dirijable.springstarter.financetracker.dto.category.CategoryUpdateDto;
import com.dirijable.springstarter.financetracker.mapper.CategoryMapper;
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
    private final CategoryMapper categoryMapper;

    public CategoryResponseDto findById(Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new RuntimeException("category with id='%d' not found".formatted(categoryId)));
        return categoryMapper.toResponse(category);
    }

    public CategoryResponseDto add(CategoryCreateDto createDto, Long userId) {
        if (categoryRepository.existsCategoriesByNameAndUserId(createDto.name(), userId)) {
            log.warn("Unable to add category with name '{}'. This category already exists", createDto.name());
            throw new IllegalArgumentException();
        }
        if (!userRepository.existsById(userId)) {
            log.warn("Unable to find user with id='{}'", userId);
            throw new RuntimeException();
        }
        Category category = categoryMapper.toEntity(createDto);
        userRepository.findById(userId).ifPresent(user -> user.addCategory(category));
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);

    }

    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId))
            throw new IllegalArgumentException("category with id='%d' does not exist".formatted(categoryId));
        categoryRepository.deleteById(categoryId);
    }

    public CategoryResponseDto updateById(CategoryUpdateDto updateDto, Long categoryId) {
        Category category= categoryRepository.findById(categoryId)
                .orElseThrow(() ->new IllegalArgumentException("category with id='%d' does not exist".formatted(categoryId)));
        categoryMapper.updateEntity(updateDto, category);
        return categoryMapper.toResponse(category);
    }
}
