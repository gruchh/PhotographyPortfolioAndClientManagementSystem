package com.gruchh.blog.core.dto.photoShoot;

import com.gruchh.blog.core.entity.ShootType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePhotoShootDto {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Shoot date is required")
    private LocalDate shootDate;

    @NotNull(message = "Shoot type is required")
    private ShootType shootType;

    private String location;
    private Boolean isActive;
    private Long clientId;
}
