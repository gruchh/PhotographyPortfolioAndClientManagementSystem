package com.gruchh.blog.core.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryDto {
    @NotBlank(message = "Name is required")
    private String name;
    private String description;

    @NotBlank(message = "Slug is required")
    private String slug;

    private Boolean isActive;
}
