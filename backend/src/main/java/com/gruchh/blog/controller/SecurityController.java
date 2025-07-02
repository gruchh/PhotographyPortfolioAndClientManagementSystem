package com.gruchh.blog.controller;


import com.gruchh.blog.dto.JwtAuthRequest;
import com.gruchh.blog.dto.JwtAuthResponse;
import com.gruchh.blog.dto.RegisterRequest;
import com.gruchh.blog.dto.UserMeResponse;
import com.gruchh.blog.security.JwtTokenValidator;
import com.gruchh.blog.service.auth.UserService;
import com.gruchh.blog.shared.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController()
@RequestMapping("/auth")
public class SecurityController {

    private final UserService userService;
    private final JwtTokenValidator jwtTokenValidator;

    @PostMapping("/register")
    public ResponseEntity<JwtAuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Processing registration request for user: {}", request.getUsername());
        try {
            String jwtToken = userService.register(request);
            log.debug("Successfully generated JWT token for user: {}", request.getUsername());
            return ResponseEntity.ok(new JwtAuthResponse(jwtToken));
        } catch (Exception e) {
            log.error("Registration failed for user: {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new JwtAuthResponse(null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody JwtAuthRequest request) {
        log.info("Processing login request for user: {}", request.getUsername());
        try {
            String jwtToken = userService.verify(request);
            log.debug("Successfully verified user and generated JWT token for: {}", request.getUsername());
            return ResponseEntity.ok(new JwtAuthResponse(jwtToken));
        } catch (Exception e) {
            log.error("Login failed for user: {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtAuthResponse(null));
        }
    }

    @GetMapping("/getCurrentUser")
    public ResponseEntity<UserMeResponse> getCurrentUser(HttpServletRequest request) {
        log.info("Przetwarzanie żądania getCurrentUser");
        try {
            String username = jwtTokenValidator.validateToken(request)
                    .orElseThrow(() -> new RuntimeException("Błąd walidacji tokenu"));
            log.debug("Zweryfikowano token dla użytkownika: {}", username);
            UserMeResponse response = userService.getCurrentUserInfo(username);
            response.setStatus("success");
            log.debug("Pobrano dane użytkownika: {}", username);
            return ResponseEntity.ok(response);
        } catch (ValidationException e) {
            log.warn("Błąd walidacji JWT: {}", e.getMessage());
            return ResponseEntity.status(e.getStatus())
                    .body(UserMeResponse.builder()
                            .status("error")
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            log.error("Błąd podczas pobierania danych użytkownika", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserMeResponse.builder()
                            .status("error")
                            .message("Wewnętrzny błąd serwera: " + e.getMessage())
                            .build());
        }
    }
}