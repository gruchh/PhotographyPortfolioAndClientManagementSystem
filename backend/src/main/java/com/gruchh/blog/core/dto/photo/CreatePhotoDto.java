package com.gruchh.blog.core.dto.photo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePhotoDto {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "File name is required")
    private String fileName;

    @NotBlank(message = "File path is required")
    private String filePath;

    private String thumbnailPath;
    private Long fileSize;
    private Integer imageWidth;
    private Integer imageHeight;
    private Boolean isPublic;
    private Boolean isFeature;
    private Long categoryId;
    private Long photoShootId;
    private Set<Long> tagIds;
}
