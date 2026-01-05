package com.dirijable.springstarter.financetracker.repository;

import com.dirijable.springstarter.financetracker.database.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long > {

    boolean existsCategoryByNameAndUserId(String name, Long userId);

    List<Category> findAllByUserId(Long userId);
}
