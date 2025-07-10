package com.gruchh.blog.core.mapper;

import com.gruchh.blog.core.dto.tag.CreateTagDto;
import com.gruchh.blog.core.dto.tag.TagResponseDto;
import com.gruchh.blog.core.dto.tag.TagSummaryDto;
import com.gruchh.blog.core.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TagMapper {

    @Mapping(target = "photosCount", expression = "java(tag.getPhotos() != null ? tag.getPhotos().size() : 0)")
    TagResponseDto toResponseDto(Tag tag);
    TagSummaryDto toSummaryDto(Tag tag);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photos", ignore = true)
    Tag toEntity(CreateTagDto createDto);
}