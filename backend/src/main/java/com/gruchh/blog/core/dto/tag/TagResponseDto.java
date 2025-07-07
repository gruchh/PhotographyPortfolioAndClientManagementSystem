package com.gruchh.blog.core.dto.tag;

public record TagResponseDto(
        Long id,
        String name,
        String slug,
        Integer photosCount
) {
}
