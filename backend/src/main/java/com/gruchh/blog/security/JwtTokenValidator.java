package com.gruchh.blog.security;

import com.gruchh.blog.service.auth.JwtService;
import com.gruchh.blog.shared.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtTokenValidator {

    private final JwtService jwtService;

    public Optional<String> validateToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ValidationException("Brak lub nieprawidłowy nagłówek Authorization", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        if (username == null || !jwtService.isTokenValid(token, username)) {
            throw new ValidationException("Nieprawidłowy lub wygasły token JWT", HttpStatus.UNAUTHORIZED);
        }

        return Optional.of(username);
    }

}
