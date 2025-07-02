package com.gruchh.blog.mapper;

import com.gruchh.blog.dto.UserMeResponse;
import com.gruchh.blog.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
public class UserResponseMapper {

    public UserMeResponse toUserMeResponse(User user) {
        log.info("Rozpoczynam mapowanie");
        return UserMeResponse.builder()
                .status("success")
                .message("Dane pobrano pomyślnie")
                .data(UserMeResponse.UserData.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .roles(user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()))
                        .createdAt(user.getCreatedAt())
                        .build())
                .build();
    }
}
