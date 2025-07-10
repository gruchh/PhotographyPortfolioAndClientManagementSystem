package com.gruchh.blog.core.service;

import com.gruchh.blog.core.dto.photo.CreatePhotoDto;
import com.gruchh.blog.core.dto.photo.PhotoSummaryDto;
import com.gruchh.blog.core.entity.Photo;
import com.gruchh.blog.core.mapper.PhotoMapper;
import com.gruchh.blog.core.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;

    @Transactional(readOnly = true)
    public Page<PhotoSummaryDto> getPhotosPage(Pageable pageable, Boolean isPublic) {
        Page<Photo> photos;
        if (isPublic != null) {
            photos = photoRepository.findByIsPublic(isPublic, pageable);
        } else {
            photos = photoRepository.findAll(pageable);
        }
        return photos.map(photoMapper::toSummaryDto);
    }

    @Transactional(readOnly = true)
    public PhotoSummaryDto getPhotoById(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found with id: " + id));
        return photoMapper.toSummaryDto(photo);
    }

    @Transactional(readOnly = true)
    public Page<PhotoSummaryDto> getFeaturedPhotos(Pageable pageable) {
        Page<Photo> photos = photoRepository.findByIsFeatured(true, pageable);
        return photos.map(photoMapper::toSummaryDto);
    }

    @Transactional(readOnly = true)
    public Page<PhotoSummaryDto> getPhotosByCategory(Long categoryId, Pageable pageable) {
        Page<Photo> photos = photoRepository.findByCategoryId(categoryId, pageable);
        return photos.map(photoMapper::toSummaryDto);
    }

    @Transactional(readOnly = true)
    public Page<PhotoSummaryDto> getPhotosByPhotoShoot(Long photoShootId, Pageable pageable) {
        Page<Photo> photos = photoRepository.findByPhotoShootId(photoShootId, pageable);
        return photos.map(photoMapper::toSummaryDto);
    }

    public PhotoSummaryDto createPhoto(CreatePhotoDto createDto) {
        if (photoRepository.existsByFileName(createDto.getFileName())) {
            throw new RuntimeException("Photo with filename '" + createDto.getFileName() + "' already exists");
        }

        Photo photo = photoMapper.toEntity(createDto);
        photo.setUploadDate(LocalDateTime.now());

        if (photo.getIsPublic() == null) {
            photo.setIsPublic(false);
        }
        if (photo.getIsFeatured() == null) {
            photo.setIsFeatured(false);
        }

        Photo savedPhoto = photoRepository.save(photo);
        log.info("Created new photo with id: {} and filename: {}", savedPhoto.getId(), savedPhoto.getFileName());

        return photoMapper.toSummaryDto(savedPhoto);
    }

    public PhotoSummaryDto updatePhoto(Long id, CreatePhotoDto updateDto) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found with id: " + id));

        photoMapper.updateEntityFromDto(updateDto, photo);

        Photo updatedPhoto = photoRepository.save(photo);
        log.info("Updated photo with id: {}", updatedPhoto.getId());

        return photoMapper.toSummaryDto(updatedPhoto);
    }

    public void deletePhoto(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found with id: " + id));

        photoRepository.delete(photo);
        log.info("Deleted photo with id: {}", id);
    }
}