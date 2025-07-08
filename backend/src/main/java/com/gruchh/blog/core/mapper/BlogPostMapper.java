package com.gruchh.blog.core.mapper;

import com.gruchh.blog.core.dto.blogPost.BlogPostResponseDto;
import com.gruchh.blog.core.dto.blogPost.BlogPostSummaryDto;
import com.gruchh.blog.core.dto.blogPost.CreateBlogPostDto;
import com.gruchh.blog.core.entity.BlogPost;
import com.gruchh.blog.security.mapper.UserMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface BlogPostMapper {

    BlogPostResponseDto toResponseDto(BlogPost blogPost);
    BlogPostSummaryDto toSummaryDto(BlogPost blogPost);
    BlogPost toEntity(CreateBlogPostDto createDto);
}