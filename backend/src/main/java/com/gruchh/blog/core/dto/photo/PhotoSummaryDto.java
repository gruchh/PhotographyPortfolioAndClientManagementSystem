package com.gruchh.blog.core.dto.photo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gruchh.blog.core.dto.category.CategorySummaryDto;
import com.gruchh.blog.core.dto.photoShoot.PhotoShootSummaryDto;

import java.time.LocalDateTime;

public record PhotoSummaryDto(
        Long id,
        String title,
        String fileName,
        String thumbnailPath,
        Integer imageWidth,
        Integer imageHeight,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime uploadDate,
        Boolean isPublic,
        Boolean isFeatured,
        CategorySummaryDto category,
        PhotoShootSummaryDto photoShoot
) {
}
