package com.gruchh.blog.security.dto;

public record UserSummaryDto(
        Long id,
        String username,
        String email
) {
}
