package com.gruchh.blog.core.dto.photoShoot;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gruchh.blog.core.entity.ShootType;
import com.gruchh.blog.security.dto.UserSummaryDto;

import java.time.LocalDate;

public record PhotoShootSummaryDto(
        Long id,
        String name,
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate shootDate,
        ShootType shootType,
        String location,
        Boolean isActive,
        UserSummaryDto client,
        Integer photosCount
) {
}
