package com.gruchh.blog.core.dto.blogPost;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gruchh.blog.security.dto.UserSummaryDto;

import java.time.LocalDateTime;

public record BlogPostSummaryDto(
        Long id,
        String title,
        String slug,
        String excerpt,
        String featuredImage,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime publishedAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        Boolean isPublished,
        Long viewCount,
        UserSummaryDto author
) {}
