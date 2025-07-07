package com.gruchh.blog.core.repository;

import com.gruchh.blog.core.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
