package com.gruchh.blog.core.service;

import com.gruchh.blog.core.dto.photoShoot.CreatePhotoShootDto;
import com.gruchh.blog.core.dto.photoShoot.PhotoShootResponseDto;
import com.gruchh.blog.core.dto.photoShoot.PhotoShootSummaryDto;
import com.gruchh.blog.core.entity.PhotoShoot;
import com.gruchh.blog.core.mapper.PhotoShootMapper;
import com.gruchh.blog.core.repository.PhotoShootRepository;
import com.gruchh.blog.security.entity.User;
import com.gruchh.blog.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PhotoShootService {

    private final PhotoShootRepository photoShootRepository;
    private final PhotoShootMapper photoShootMapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<PhotoShootSummaryDto> getPhotoShootsPage(Pageable pageable, Boolean active) {
        Page<PhotoShoot> photoShoots;
        if (active != null) {
            photoShoots = photoShootRepository.findByIsActive(active, pageable);
        } else {
            photoShoots = photoShootRepository.findAll(pageable);
        }
        return photoShoots.map(photoShootMapper::toSummaryDto);
    }

    @Transactional(readOnly = true)
    public PhotoShootResponseDto getPhotoShootById(Long id) {
        PhotoShoot photoShoot = photoShootRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PhotoShoot not found with id: " + id));
        return photoShootMapper.toResponseDto(photoShoot);
    }

    public PhotoShootResponseDto createPhotoShoot(CreatePhotoShootDto createDto) {
        User client;
        if (createDto.getClientId() != null) {
            client = userService.getUserById(createDto.getClientId());
        } else {
            client = userService.getCurrentUser();
        }

        PhotoShoot photoShoot = photoShootMapper.toEntity(createDto);
        photoShoot.setClient(client);

        PhotoShoot savedPhotoShoot = photoShootRepository.save(photoShoot);
        log.info("Created new photo shoot with id: {} for client: {}", savedPhotoShoot.getId(), client.getUsername());

        return photoShootMapper.toResponseDto(savedPhotoShoot);
    }

    public void deletePhotoShoot(Long id) {
        PhotoShoot photoShoot = photoShootRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PhotoShoot not found with id: " + id));

        photoShootRepository.delete(photoShoot);
        log.info("Deleted photo shoot with id: {}", id);
    }
}