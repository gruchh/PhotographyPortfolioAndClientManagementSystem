package com.gruchh.blog.security.service;

import com.gruchh.blog.core.entity.Role;
import com.gruchh.blog.security.config.JwtProperties;
import com.gruchh.blog.security.exception.JwtException;
import com.gruchh.blog.security.exception.JwtTokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generateToken(String username, String email, Set<Role> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("roles", roles.stream()
                .map(Role::name)
                .collect(Collectors.toSet()));

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuer(jwtProperties.getIssuer())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getDuration()))
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
        log.debug("Retrieving JWT key, length: {} bytes", keyBytes.length);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    public Set<Role> extractRoles(String token) {
        return extractClaim(token, claims -> {
            Object rolesObj = claims.get("roles");
            if (rolesObj instanceof Collection<?>) {
                Collection<?> collection = (Collection<?>) rolesObj;
                return collection.stream()
                        .filter(item -> item instanceof String)
                        .map(item -> (String) item)
                        .map(Role::valueOf)
                        .collect(Collectors.toSet());
            }
            return new HashSet<>();
        });
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.error("JWT token has expired: {}", e.getMessage());
            throw new JwtTokenExpiredException("JWT token has expired");
        } catch (Exception e) {
            log.error("Error parsing JWT token: {}", e.getMessage());
            throw new JwtException("Invalid JWT token", e);
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String userName = extractUsername(token);
            return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtTokenExpiredException e) {
            log.error("JWT token validation failed - token expired for user: {}", userDetails.getUsername());
            return false;
        } catch (JwtException e) {
            log.error("JWT token validation failed for user {}: {}", userDetails.getUsername(), e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error during JWT token validation for user {}: {}", userDetails.getUsername(), e.getMessage());
            return false;
        }
    }

    public boolean isTokenValid(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        } catch (JwtTokenExpiredException e) {
            log.error("JWT token validation failed - token expired for user: {}", username);
            return false;
        } catch (JwtException e) {
            log.error("JWT token validation failed for user {}: {}", username, e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error during JWT token validation for user {}: {}", username, e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            log.debug("Token is expired: {}", e.getMessage());
            return true;
        }
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (JwtTokenExpiredException e) {
            log.error("JWT token validity check failed - token expired: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.error("JWT token validity check failed: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error during JWT token validity check: {}", e.getMessage());
            return false;
        }
    }

    public long getTokenDurationTime() {
        return jwtProperties.getDuration();
    }
}