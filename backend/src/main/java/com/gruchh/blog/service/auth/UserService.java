package com.gruchh.blog.service.auth;

import com.gruchh.blog.dto.JwtAuthRequest;
import com.gruchh.blog.dto.RegisterRequest;
import com.gruchh.blog.dto.UserMeResponse;
import com.gruchh.blog.entity.Role;
import com.gruchh.blog.entity.User;
import com.gruchh.blog.mapper.UserResponseMapper;
import com.gruchh.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserResponseMapper userResponseMapper;

    @Transactional
    public String register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalStateException("User with this username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("User with this email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .roles(Set.of(Role.USER))
                .build();

        userRepository.save(user);

        return verify(new JwtAuthRequest(request.getUsername(), request.getPassword()));
    }

    public String verify(JwtAuthRequest request) {
        User existingUser = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(
                    existingUser.getUsername(),
                    existingUser.getEmail(),
                    existingUser.getRoles()
            );
        } else {
            throw new BadCredentialsException("Authentication failed for user: " + request.getUsername());
        }
    }

    public UserMeResponse getCurrentUserInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + username));
    return userResponseMapper.toUserMeResponse(user);

    }
}