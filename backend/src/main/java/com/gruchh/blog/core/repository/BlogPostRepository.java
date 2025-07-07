package com.gruchh.blog.core.repository;

import com.gruchh.blog.core.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository <BlogPost, Long> {
}
