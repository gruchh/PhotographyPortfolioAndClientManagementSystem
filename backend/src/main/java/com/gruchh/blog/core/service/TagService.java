package com.gruchh.blog.core.service;

import com.gruchh.blog.core.dto.tag.CreateTagDto;
import com.gruchh.blog.core.dto.tag.TagResponseDto;
import com.gruchh.blog.core.dto.tag.TagSummaryDto;
import com.gruchh.blog.core.entity.Tag;
import com.gruchh.blog.core.mapper.TagMapper;
import com.gruchh.blog.core.repository.TagRepository;
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
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Transactional(readOnly = true)
    public Page<TagSummaryDto> getTagsPage(Pageable pageable) {
        Page<Tag> tags = tagRepository.findAll(pageable);
        return tags.map(tagMapper::toSummaryDto);
    }

    @Transactional(readOnly = true)
    public TagResponseDto getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found with id: " + id));
        return tagMapper.toResponseDto(tag);
    }

    @Transactional(readOnly = true)
    public TagResponseDto getTagBySlug(String slug) {
        Tag tag = tagRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Tag not found with slug: " + slug));
        return tagMapper.toResponseDto(tag);
    }

    public TagResponseDto createTag(CreateTagDto createDto) {
        if (tagRepository.existsBySlug(createDto.getSlug())) {
            throw new RuntimeException("Tag with slug '" + createDto.getSlug() + "' already exists");
        }

        if (tagRepository.existsByName(createDto.getName())) {
            throw new RuntimeException("Tag with name '" + createDto.getName() + "' already exists");
        }

        Tag tag = tagMapper.toEntity(createDto);
        Tag savedTag = tagRepository.save(tag);

        log.info("Created new tag with id: {} and name: {}", savedTag.getId(), savedTag.getName());
        return tagMapper.toResponseDto(savedTag);
    }

    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found with id: " + id));

        if (!tag.getPhotos().isEmpty()) {
            throw new RuntimeException("Cannot delete tag that is assigned to photos");
        }

        tagRepository.delete(tag);
        log.info("Deleted tag with id: {} and name: {}", id, tag.getName());
    }
}