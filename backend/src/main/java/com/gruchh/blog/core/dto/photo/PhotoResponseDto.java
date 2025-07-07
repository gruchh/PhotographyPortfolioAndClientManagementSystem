package com.gruchh.blog.core.dto.photo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gruchh.blog.core.dto.category.CategorySummaryDto;
import com.gruchh.blog.core.dto.photoShoot.PhotoShootSummaryDto;
import com.gruchh.blog.core.dto.tag.TagSummaryDto;

import java.time.LocalDateTime;
import java.util.Set;

public record PhotoResponseDto(
        Long id,
        String title,
        String description,
        String fileName,
        String filePath,
        String thumbnailPath,
        Long fileSize,
        Integer imageWidth,
        Integer imageHeight,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime uploadDate,
        Boolean isPublic,
        Boolean isFeatured,
        CategorySummaryDto category,
        PhotoShootSummaryDto photoShoot,
        Set<TagSummaryDto> tags
) {}

