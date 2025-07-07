package com.gruchh.blog.core.dto.photoShoot;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gruchh.blog.core.dto.photo.PhotoSummaryDto;
import com.gruchh.blog.core.entity.ShootType;
import com.gruchh.blog.security.dto.UserSummaryDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PhotoShootResponseDto(
        Long id,
        String name,
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate shootDate,
        ShootType shootType,
        String location,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,
        Boolean isActive,
        UserSummaryDto client,
        List<PhotoSummaryDto> photos,
        Integer photosCount
) {
}
