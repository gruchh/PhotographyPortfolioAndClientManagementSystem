package com.gruchh.blog.core.dto.blogPost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBlogPostDto {
    private String title;
    private String slug;
    private String content;
    private String excerpt;
    private String featuredImage;
    private Boolean isPublished ;
    private LocalDateTime publishedAt;
}