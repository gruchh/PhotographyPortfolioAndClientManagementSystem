package com.gruchh.blog.core.mapper;

import com.gruchh.blog.core.dto.photo.CreatePhotoDto;
import com.gruchh.blog.core.dto.photo.PhotoSummaryDto;
import com.gruchh.blog.core.entity.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

    PhotoSummaryDto toSummaryDto(Photo photo);
    Photo toEntity(CreatePhotoDto createDto);
    void updateEntityFromDto(CreatePhotoDto updateDto, @MappingTarget Photo photo);
}