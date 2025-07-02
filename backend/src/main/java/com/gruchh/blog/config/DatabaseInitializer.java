package com.gruchh.blog.config;

import com.gruchh.blog.entity.Role;
import com.gruchh.blog.entity.User;
import com.gruchh.blog.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void saveExampleUsers() {
        User us1 = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .email("admin@add.pl")
                .roles(Set.of(Role.ADMIN, Role.USER))
                .build();

        User us2 = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .email("user@add.pl")
                .roles(Set.of(Role.USER))
                .build();
        userRepository.saveAll(List.of(us1, us2));
        log.info("Users saved");
    }
}
