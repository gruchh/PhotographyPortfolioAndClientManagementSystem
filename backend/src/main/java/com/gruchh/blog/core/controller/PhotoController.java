package com.gruchh.blog.core.controller;

import com.gruchh.blog.core.dto.photo.CreatePhotoDto;
import com.gruchh.blog.core.dto.photo.PhotoSummaryDto;
import com.gruchh.blog.core.service.PhotoService;
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
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @GetMapping
    public ResponseEntity<Page<PhotoSummaryDto>> getAllPhotos(
            @PageableDefault(size = 10, sort = "uploadDate") Pageable pageable,
            @RequestParam(required = false) Boolean isPublic) {
        Page<PhotoSummaryDto> photos = photoService.getPhotosPage(pageable, isPublic);
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoSummaryDto> getPhotoById(@PathVariable Long id) {
        PhotoSummaryDto photo = photoService.getPhotoById(id);
        return ResponseEntity.ok(photo);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PhotoSummaryDto> createPhoto(@Valid @RequestBody CreatePhotoDto createDto) {
        PhotoSummaryDto createdPhoto = photoService.createPhoto(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPhoto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PhotoSummaryDto> updatePhoto(
            @PathVariable Long id,
            @Valid @RequestBody CreatePhotoDto updateDto) {
        PhotoSummaryDto updatedPhoto = photoService.updatePhoto(id, updateDto);
        return ResponseEntity.ok(updatedPhoto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/featured")
    public ResponseEntity<Page<PhotoSummaryDto>> getFeaturedPhotos(
            @PageableDefault(size = 10, sort = "uploadDate") Pageable pageable) {
        Page<PhotoSummaryDto> featuredPhotos = photoService.getFeaturedPhotos(pageable);
        return ResponseEntity.ok(featuredPhotos);
    }


}