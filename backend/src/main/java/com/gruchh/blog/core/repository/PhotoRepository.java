package com.gruchh.blog.core.repository;

import com.gruchh.blog.core.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
