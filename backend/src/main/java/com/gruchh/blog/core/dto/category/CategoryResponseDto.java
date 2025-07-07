package com.gruchh.blog.core.dto.category;

public record CategoryResponseDto(
        Long id,
        String name,
        String description,
        String slug,
        Boolean isActive,
        Integer photosCount
) {}
