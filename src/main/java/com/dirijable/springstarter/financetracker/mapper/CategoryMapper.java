package com.dirijable.springstarter.financetracker.mapper;

import com.dirijable.springstarter.financetracker.database.entity.Category;
import com.dirijable.springstarter.financetracker.dto.category.CategoryCreateDto;
import com.dirijable.springstarter.financetracker.dto.category.CategoryResponseDto;
import com.dirijable.springstarter.financetracker.dto.category.CategoryUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CategoryMapper {

    Category toEntity(CategoryCreateDto createDto);

    void updateEntity(CategoryUpdateDto updateDto, @MappingTarget Category category);

    CategoryResponseDto toResponse(Category category);
}

//
//public Category toEntity(CategoryCreateDto createDto){
//    return Category.builder()
//            .name(createDto.name())
//            .description(createDto.description())
//            .build();
//}
//
//public void updateEntity(CategoryUpdateDto updateDto, Category category){
//    if(updateDto.name() != null)
//        category.setName(updateDto.name());
//    if(updateDto.description() != null)
//        category.setDescription(updateDto.description());
//}
//
//public CategoryResponseDto toResponse(Category category){
//    return new CategoryResponseDto(
//            category.getId(),
//            category.getName(),
//            category.getDescription()
//    );
//}