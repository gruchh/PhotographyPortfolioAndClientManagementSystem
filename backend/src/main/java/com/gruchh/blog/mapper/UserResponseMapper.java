package com.gruchh.blog.mapper;

import com.gruchh.blog.dto.UserProfileDto;
import com.gruchh.blog.dto.UserProfileResponse;
import com.gruchh.blog.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
public class UserResponseMapper {

    public UserProfileResponse toUserProfileResponse(User user) {
        log.info("Start mapping");
        return UserProfileResponse.builder()
                .status("success")
                .message("Data is complete")
                .data(UserProfileDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .roles(user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()))
                        .createdAt(user.getCreatedAt())
                        .build())
                .build();
    }
}
