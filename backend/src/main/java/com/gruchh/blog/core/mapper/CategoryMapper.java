package com.gruchh.blog.core.mapper;

import com.gruchh.blog.core.dto.category.CategoryResponseDto;
import com.gruchh.blog.core.dto.category.CategorySummaryDto;
import com.gruchh.blog.core.dto.category.CreateCategoryDto;
import com.gruchh.blog.core.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDto toResponseDto(Category category);
    CategorySummaryDto toCategorySummaryDto(Category category);
    Category toEntity(CreateCategoryDto createDto);
    void updateEntityFromDto(CreateCategoryDto updateDto, @MappingTarget Category category);
}