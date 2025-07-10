package com.gruchh.blog.core.repository;

import com.gruchh.blog.core.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Page<Photo> findByIsPublic(Boolean isPublic, Pageable pageable);
    Page<Photo> findByIsFeatured(Boolean isFeatured, Pageable pageable);
    Page<Photo> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Photo> findByPhotoShootId(Long photoShootId, Pageable pageable);
    boolean existsByFileName(String fileName);
}