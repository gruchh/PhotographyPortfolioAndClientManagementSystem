package com.gruchh.blog.core.controller;

import com.gruchh.blog.core.dto.tag.CreateTagDto;
import com.gruchh.blog.core.dto.tag.TagResponseDto;
import com.gruchh.blog.core.dto.tag.TagSummaryDto;
import com.gruchh.blog.core.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<Page<TagSummaryDto>> getAllTags(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        Page<TagSummaryDto> tags = tagService.getTagsPage(pageable);
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponseDto> getTagById(@PathVariable Long id) {
        TagResponseDto tag = tagService.getTagById(id);
        return ResponseEntity.ok(tag);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<TagResponseDto> getTagBySlug(@PathVariable String slug) {
        TagResponseDto tag = tagService.getTagBySlug(slug);
        return ResponseEntity.ok(tag);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagResponseDto> createTag(@Valid @RequestBody CreateTagDto createDto) {
        TagResponseDto createdTag = tagService.createTag(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}