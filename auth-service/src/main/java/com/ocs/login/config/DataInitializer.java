package com.ocs.login.config;

import com.ocs.login.entity.Role;
import com.ocs.login.entity.User;
import com.ocs.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initializeSuperAdmin() {
        return args -> {
            String superAdminEmail = "superadmin@gmail.com";

            userRepository.findByEmail(superAdminEmail).ifPresentOrElse(
                    user -> log.info("Super Admin already exists: {}", superAdminEmail),
                    () -> {
                        User user = User.builder()
                                .email(superAdminEmail)
                                .firstName("Super")
                                .lastName("Admin")
                                .password(passwordEncoder.encode("admin"))
                                .role(Role.SUPER_ADMIN)
                                .build();

                        userRepository.save(user);
                        log.info("✅ Super Admin created: {}", superAdminEmail);
                    }
            );
        };
    }
}
