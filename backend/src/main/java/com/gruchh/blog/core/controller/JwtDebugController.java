package com.gruchh.blog.core.controller;

import com.gruchh.blog.core.entity.Role;
import com.gruchh.blog.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
public class JwtDebugController {

    private final JwtService jwtService;

    @GetMapping("/token-info")
    public TokenInfo getTokenInfo(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7); // Remove "Bearer " prefix

            String username = jwtService.extractUsername(token);
            String email = jwtService.extractEmail(token);
            Set<Role> roles = jwtService.extractRoles(token);
            boolean isValid = jwtService.isTokenValid(token);

            return new TokenInfo(username, email, roles, isValid);

        } catch (Exception e) {
            log.error("Error extracting token info: {}", e.getMessage());
            return new TokenInfo("ERROR", "ERROR", Set.of(), false);
        }
    }

    @GetMapping("/current-user")
    public CurrentUserInfo getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return new CurrentUserInfo("No authentication", Set.of(), false);
        }

        String username = auth.getName();
        Set<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        boolean hasAdminRole = authorities.contains("ROLE_ADMIN") || authorities.contains("admin");

        return new CurrentUserInfo(username, authorities, hasAdminRole);
    }

    public record TokenInfo(String username, String email, Set<Role> roles, boolean isValid) {}
    public record CurrentUserInfo(String username, Set<String> authorities, boolean hasAdminRole) {}
}