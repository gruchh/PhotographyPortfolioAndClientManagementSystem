package com.gruchh.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMeResponse {

    private String status;
    private String message;
    private UserData data;

    @Getter
    @Builder
    public static class UserData {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
    }
}
