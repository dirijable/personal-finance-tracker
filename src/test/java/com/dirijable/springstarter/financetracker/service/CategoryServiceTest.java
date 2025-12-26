package com.dirijable.springstarter.financetracker.service;

import com.dirijable.springstarter.financetracker.database.entity.Category;
import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.repository.CategoryRepository;
import com.dirijable.springstarter.financetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;

    @Test
    void checkAddCategory() {
        String categoryName = "Category 1";
        User user = User.builder()
                .id(1L)
                .username("username 1")
                .email("email@gmail.com")
                .build();
        Category savedCategory = Category.builder()
                .id(1L)
                .name(categoryName)
                .user(user)
                .build();
        when(categoryRepository.existsCategoriesByNameAndUserId(categoryName, 1L))
                .thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.save(any())).thenReturn(savedCategory);
        Category category = categoryService.addCategory(categoryName, 1L);

        assertThat(category.getUser()).isEqualTo(user);
        assertThat(user.getCategories()).hasSize(1);
        verify(categoryRepository).save(any(Category.class));
    }

}