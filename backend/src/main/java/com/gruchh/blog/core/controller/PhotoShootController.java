package com.gruchh.blog.core.controller;

import com.gruchh.blog.core.dto.photoShoot.CreatePhotoShootDto;
import com.gruchh.blog.core.dto.photoShoot.PhotoShootResponseDto;
import com.gruchh.blog.core.dto.photoShoot.PhotoShootSummaryDto;
import com.gruchh.blog.core.service.PhotoShootService;
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
@RequestMapping("/api/photo-shoots")
@RequiredArgsConstructor
public class PhotoShootController {

    private final PhotoShootService photoShootService;

    @GetMapping
    public ResponseEntity<Page<PhotoShootSummaryDto>> getAllPhotoShoots(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable,
            @RequestParam(required = false) Boolean active) {
        Page<PhotoShootSummaryDto> photoShoots = photoShootService.getPhotoShootsPage(pageable, active);
        return ResponseEntity.ok(photoShoots);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoShootResponseDto> getPhotoShootById(@PathVariable Long id) {
        PhotoShootResponseDto photoShoot = photoShootService.getPhotoShootById(id);
        return ResponseEntity.ok(photoShoot);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PhotoShootResponseDto> createPhotoShoot(@Valid @RequestBody CreatePhotoShootDto createDto) {
        PhotoShootResponseDto createdPhotoShoot = photoShootService.createPhotoShoot(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPhotoShoot);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePhotoShoot(@PathVariable Long id) {
        photoShootService.deletePhotoShoot(id);
        return ResponseEntity.noContent().build();
    }
}