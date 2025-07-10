package com.gruchh.blog.core.mapper;

import com.gruchh.blog.core.dto.photoShoot.CreatePhotoShootDto;
import com.gruchh.blog.core.dto.photoShoot.PhotoShootResponseDto;
import com.gruchh.blog.core.dto.photoShoot.PhotoShootSummaryDto;
import com.gruchh.blog.core.entity.PhotoShoot;
import com.gruchh.blog.security.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PhotoShootMapper {

    @Mapping(target = "photosCount", expression = "java(photoShoot.getPhotos() != null ? photoShoot.getPhotos().size() : 0)")
    PhotoShootResponseDto toResponseDto(PhotoShoot photoShoot);

    @Mapping(target = "photosCount", expression = "java(photoShoot.getPhotos() != null ? photoShoot.getPhotos().size() : 0)")
    PhotoShootSummaryDto toSummaryDto(PhotoShoot photoShoot);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "photos", ignore = true)
    PhotoShoot toEntity(CreatePhotoShootDto createDto);
}