package com.dirijable.springstarter.financetracker.service;

import com.dirijable.springstarter.financetracker.database.entity.Category;
import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.dto.category.CategoryCreateDto;
import com.dirijable.springstarter.financetracker.dto.category.CategoryResponseDto;
import com.dirijable.springstarter.financetracker.dto.category.CategoryUpdateDto;
import com.dirijable.springstarter.financetracker.exception.business.conflict.CategoryAlreadyExistException;
import com.dirijable.springstarter.financetracker.exception.business.denied.AccessDeniedException;
import com.dirijable.springstarter.financetracker.exception.business.notfound.CategoryNotFoundException;
import com.dirijable.springstarter.financetracker.exception.business.notfound.UserNotFoundException;
import com.dirijable.springstarter.financetracker.mapper.CategoryMapper;
import com.dirijable.springstarter.financetracker.repository.CategoryRepository;
import com.dirijable.springstarter.financetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    @PreAuthorize("#userId == authentication.principal.id")
    public List<CategoryResponseDto> findAllByUserId(Long userId) {
        return categoryRepository.findAllByUserId(userId)
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @PreAuthorize("@securityService.canAccessCategory(#categoryId)")
    public CategoryResponseDto findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId.toString()));
    }


    @PreAuthorize("#userId == authentication.principal.id")
    @Transactional
    public CategoryResponseDto add(CategoryCreateDto createDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User with id = '%d' not found".formatted(userId))
        );
        if (categoryRepository.existsCategoryByNameAndUserId(createDto.name(), userId)) {
            throw new CategoryAlreadyExistException("Unable to add category with name '%s'. This category already exists".formatted(createDto.name()));
        }
        Category category = categoryMapper.toEntity(createDto);
        user.addCategory(category);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    @PreAuthorize("@securityService.canAccessCategory(#categoryId)")
    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }


    @PreAuthorize("@securityService.canAccessCategory(#categoryId)")
    @Transactional
    public CategoryResponseDto updateById(CategoryUpdateDto updateDto, Long categoryId, Long userId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId.toString()));
        if (categoryRepository.existsCategoryByNameAndUserId(updateDto.name(), userId))
            throw new CategoryAlreadyExistException("Unable to add category with name '%s'. This category already exists".formatted(updateDto.name()));
        categoryMapper.updateEntity(updateDto, category);
        return categoryMapper.toResponse(category);
    }
}
