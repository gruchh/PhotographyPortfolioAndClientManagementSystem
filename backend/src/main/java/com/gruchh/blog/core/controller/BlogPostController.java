package com.gruchh.blog.core.controller;

import com.gruchh.blog.core.dto.blogPost.BlogPostResponseDto;
import com.gruchh.blog.core.dto.blogPost.BlogPostSummaryDto;
import com.gruchh.blog.core.dto.blogPost.CreateBlogPostDto;
import com.gruchh.blog.core.service.BlogPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog-posts")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @GetMapping
    public ResponseEntity<Page<BlogPostSummaryDto>> getAllPosts(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable,
            @RequestParam(required = false) Boolean published) {
        Page<BlogPostSummaryDto> posts = blogPostService.getPostsPage(pageable, published);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostResponseDto> getPostById(@PathVariable Long id) {
        BlogPostResponseDto post = blogPostService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<BlogPostResponseDto> getPostBySlug(@PathVariable String slug) {
        BlogPostResponseDto post = blogPostService.getPostBySlug(slug);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BlogPostResponseDto> createPost(@Valid @RequestBody CreateBlogPostDto createDto) {
        BlogPostResponseDto createdPost = blogPostService.createPost(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        blogPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}