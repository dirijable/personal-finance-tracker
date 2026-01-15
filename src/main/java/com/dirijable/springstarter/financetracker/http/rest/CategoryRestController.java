package com.dirijable.springstarter.financetracker.http.rest;

import com.dirijable.springstarter.financetracker.database.entity.User;
import com.dirijable.springstarter.financetracker.dto.category.CategoryCreateDto;
import com.dirijable.springstarter.financetracker.dto.category.CategoryResponseDto;
import com.dirijable.springstarter.financetracker.dto.category.CategoryUpdateDto;
import com.dirijable.springstarter.financetracker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> findAllByUserId(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(categoryService.findAllByUserId(user.getId()));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> findById(@PathVariable("categoryId") Long categoryId ){
        return ResponseEntity.ok(categoryService.findById(categoryId));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@AuthenticationPrincipal User user,
                                                      @RequestBody @Validated CategoryCreateDto createDto) {
        CategoryResponseDto response = categoryService.add(createDto, user.getId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri)
                .body(response);
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateById(@PathVariable("categoryId") Long categoryId,
                                                          @AuthenticationPrincipal User user,
                                                          @RequestBody @Validated CategoryUpdateDto updateDto) {
        CategoryResponseDto responseDto = categoryService.updateById(updateDto, categoryId, user.getId());
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteById(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
