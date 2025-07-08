package com.gruchh.blog.core.repository;

import com.gruchh.blog.core.entity.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    Optional<BlogPost> findBySlug(String slug);
    Page<BlogPost> findByIsPublished(Boolean isPublished, Pageable pageable);
    boolean existsBySlug(String slug);
}
