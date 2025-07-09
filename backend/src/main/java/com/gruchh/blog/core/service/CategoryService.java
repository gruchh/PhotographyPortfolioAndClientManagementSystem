package com.gruchh.blog.core.service;

import com.gruchh.blog.core.dto.category.CategoryResponseDto;
import com.gruchh.blog.core.dto.category.CreateCategoryDto;
import com.gruchh.blog.core.entity.Category;
import com.gruchh.blog.core.mapper.CategoryMapper;
import com.gruchh.blog.core.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return categoryMapper.toResponseDto(category);
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Category not found with slug: " + slug));
        return categoryMapper.toResponseDto(category);
    }

    public CategoryResponseDto createCategory(CreateCategoryDto createDto) {
        if (categoryRepository.existsBySlug(createDto.getSlug())) {
            throw new RuntimeException("Category with slug '" + createDto.getSlug() + "' already exists");
        }

        if (categoryRepository.existsByName(createDto.getName())) {
            throw new RuntimeException("Category with name '" + createDto.getName() + "' already exists");
        }

        Category category = categoryMapper.toEntity(createDto);
        Category savedCategory = categoryRepository.save(category);

        log.info("Created new category with id: {} and name: {}", savedCategory.getId(), savedCategory.getName());
        return categoryMapper.toResponseDto(savedCategory);
    }

    public CategoryResponseDto updateCategory(Long id, CreateCategoryDto updateDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        if (!category.getSlug().equals(updateDto.getSlug()) &&
                categoryRepository.existsBySlug(updateDto.getSlug())) {
            throw new RuntimeException("Category with slug '" + updateDto.getSlug() + "' already exists");
        }

        if (!category.getName().equals(updateDto.getName()) &&
                categoryRepository.existsByName(updateDto.getName())) {
            throw new RuntimeException("Category with name '" + updateDto.getName() + "' already exists");
        }

        categoryMapper.updateEntityFromDto(updateDto, category);
        Category updatedCategory = categoryRepository.save(category);

        log.info("Updated category with id: {} and name: {}", updatedCategory.getId(), updatedCategory.getName());
        return categoryMapper.toResponseDto(updatedCategory);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setIsActive(false);
        categoryRepository.save(category);

        log.info("Deactivated category with id: {} and name: {}", id, category.getName());
    }

    public void forceDeleteCategory(Long id, Long newCategoryId) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        if (!category.getPhotos().isEmpty()) {
            if (newCategoryId != null) {
                Category newCategory = categoryRepository.findById(newCategoryId)
                        .orElseThrow(() -> new RuntimeException("Target category not found with id: " + newCategoryId));

                category.getPhotos().forEach(photo -> photo.setCategory(newCategory));
                log.info("Moved {} photos from category {} to category {}",
                        category.getPhotos().size(), category.getName(), newCategory.getName());
            } else {
                throw new RuntimeException("Cannot delete category with photos. Please specify target category or move photos first.");
            }
        }

        categoryRepository.delete(category);
        log.info("Force deleted category with id: {} and name: {}", id, category.getName());
    }
}