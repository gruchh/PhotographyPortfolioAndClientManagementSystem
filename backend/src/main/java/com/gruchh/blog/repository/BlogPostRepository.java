package com.gruchh.blog.repository;

import com.gruchh.blog.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository <BlogPost, Long> {
}
