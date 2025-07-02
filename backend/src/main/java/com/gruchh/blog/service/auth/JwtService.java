package com.gruchh.blog.service.auth;

import com.gruchh.blog.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.token.secretKey}")
    private String secretkey;

    @Value("${jwt.token.duration:3600000}")
    private Long tokenDurationTime;

    @PostConstruct
    public void init() {
        if (secretkey == null || secretkey.length() < 32) {
            log.info("Wczytany klucz JWT: {}", secretkey);
            throw new IllegalStateException("Klucz JWT musi mieć co najmniej 32 znaki dla HS256");
        }
        log.info("JwtService zainicjalizowany z kluczem o długości {} znaków", secretkey.length());
    }

    public String generateToken(String username, String email, Set<Role> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("roles", roles.stream()
                .map(Role::name)
                .collect(Collectors.toSet()));

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenDurationTime))
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = secretkey.getBytes(StandardCharsets.UTF_8);
        log.debug("Pobieranie klucza JWT, długość: {} bajtów", keyBytes.length);
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
        } catch (Exception e) {
            log.error("Błąd podczas parsowania tokenu JWT: {}", e.getMessage());
            throw new RuntimeException("Nieprawidłowy token JWT", e);
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String userName = extractUsername(token);
            return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            log.error("Błąd walidacji tokenu JWT: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTokenValid(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        } catch (Exception e) {
            log.error("Błąd walidacji tokenu JWT dla użytkownika {}: {}", username, e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.error("Błąd sprawdzania ważności tokenu JWT: {}", e.getMessage());
            return false;
        }
    }

    public long getTokenDurationTime() {
        return tokenDurationTime;
    }
}