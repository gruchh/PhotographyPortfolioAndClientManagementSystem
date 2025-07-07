package com.gruchh.blog.core.dto.category;

public record CategorySummaryDto(
        Long id,
        String name,
        String slug,
        Boolean isActive
) {}
