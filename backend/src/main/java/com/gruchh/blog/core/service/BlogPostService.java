package com.gruchh.blog.core.service;

import com.gruchh.blog.core.dto.blogPost.BlogPostResponseDto;
import com.gruchh.blog.core.dto.blogPost.BlogPostSummaryDto;
import com.gruchh.blog.core.dto.blogPost.CreateBlogPostDto;
import com.gruchh.blog.core.entity.BlogPost;
import com.gruchh.blog.core.mapper.BlogPostMapper;
import com.gruchh.blog.core.repository.BlogPostRepository;
import com.gruchh.blog.security.entity.User;
import com.gruchh.blog.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final BlogPostMapper blogPostMapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<BlogPostSummaryDto> getPostsPage(Pageable pageable, Boolean published) {
        Page<BlogPost> posts;
        if (published != null) {
            posts = blogPostRepository.findByIsPublished(published, pageable);
        } else {
            posts = blogPostRepository.findAll(pageable);
        }
        return posts.map(blogPostMapper::toSummaryDto);
    }

    @Transactional(readOnly = true)
    public BlogPostResponseDto getPostById(Long id) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        return blogPostMapper.toResponseDto(post);
    }

    @Transactional(readOnly = true)
    public BlogPostResponseDto getPostBySlug(String slug) {
        BlogPost post = blogPostRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Post not found with slug: " + slug));
        return blogPostMapper.toResponseDto(post);
    }

    public BlogPostResponseDto createPost(CreateBlogPostDto createDto) {
        if (blogPostRepository.existsBySlug(createDto.getSlug())) {
            throw new RuntimeException("Post with slug '" + createDto.getSlug() + "' already exists");
        }

        User currentUser = userService.getCurrentUser();

        BlogPost blogPost = blogPostMapper.toEntity(createDto);
        blogPost.setAuthor(currentUser);

        if (Boolean.TRUE.equals(createDto.getIsPublished()) && createDto.getPublishedAt() == null) {
            blogPost.setPublishedAt(LocalDateTime.now());
        }

        BlogPost savedPost = blogPostRepository.save(blogPost);
        log.info("Created new blog post with id: {} by user: {}", savedPost.getId(), currentUser.getUsername());

        return blogPostMapper.toResponseDto(savedPost);
    }

    public void deletePost(Long id) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        blogPostRepository.delete(post);
        log.info("Deleted blog post with id: {}", id);
    }
}