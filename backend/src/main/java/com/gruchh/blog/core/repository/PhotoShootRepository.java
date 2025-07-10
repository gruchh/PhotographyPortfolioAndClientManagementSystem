package com.gruchh.blog.core.repository;

import com.gruchh.blog.core.entity.PhotoShoot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoShootRepository extends JpaRepository<PhotoShoot, Long> {

    Page<PhotoShoot> findByIsActive(Boolean isActive, Pageable pageable);
}