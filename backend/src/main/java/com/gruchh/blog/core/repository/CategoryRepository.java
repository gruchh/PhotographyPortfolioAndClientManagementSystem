package com.gruchh.blog.core.repository;

import com.gruchh.blog.core.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
