package com.gruchh.blog.core.dto.blogPost;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gruchh.blog.security.dto.UserSummaryDto;

import java.time.LocalDateTime;

public class BlogPostSummaryDto {
    private Long id;
    private String title;
    private String slug;
    private String excerpt;
    private String featuredImage;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Boolean isPublished;
    private Long viewCount;

    private UserSummaryDto author;
}