package com.ocs.login.service.impl;

import com.ocs.login.dto.*;
import com.ocs.login.entity.Role;
import com.ocs.login.entity.User;
import com.ocs.login.exception.auth.UnauthorizedException;
import com.ocs.login.repository.UserRepository;
import com.ocs.login.service.AuthenticationService;
import com.ocs.login.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public User signup(SignUpRequest signUpRequest) {
        log.info("Attempting to register user with email: {}", signUpRequest.getEmail());
        User savedUser = createUserEntity(signUpRequest, Role.USER);
        log.info("User registered successfully: {}", savedUser.getEmail());
        return userRepository.save(savedUser);
    }

    @Override
    @Transactional
    public User signupAdmin(SignUpRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentRoleStr = auth.getAuthorities().iterator().next().getAuthority();
        Role currentRole = Role.valueOf(currentRoleStr.replace("ROLE_", ""));
        Role targetRole = request.getRole();

        log.info("User with role '{}' is creating a new user with role '{}'", currentRole, targetRole);

        if (!isRoleAssignmentAllowed(currentRole, targetRole)) {
            log.warn("Unauthorized role creation attempt by '{}'", currentRole);
            throw new UnauthorizedException();
//            throw new UnauthorizedException("You are not authorized to assign this role.");
        }

        User user = createUserEntity(request, targetRole);
        User savedUser = userRepository.save(user);
        log.info("Admin created user: {} with role: {}", savedUser.getEmail(), savedUser.getRole());
        return savedUser;
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest signInRequest) {
        log.info("User attempting sign-in: {}", signInRequest.getEmail());

        authenticateUser(signInRequest.getEmail(), signInRequest.getPassword());

        User user = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> {
                    log.error("Sign-in failed. Email not found: {}", signInRequest.getEmail());
                    return new IllegalArgumentException("Invalid email or password");
                });
        log.info("User signed in successfully: {}", user.getEmail());
        return buildJwtResponse(user);
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
        log.info("Refreshing token for user: {}", userEmail);
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null || !jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            log.warn("Invalid refresh token for user: {}", userEmail);
            throw new UnauthorizedException();
//            throw new UnauthorizedException("Invalid refresh token");
        }
        String newJwt = jwtService.generateToken(user);
        return buildJwtResponse(user, newJwt, refreshTokenRequest.getToken());
    }

    // ---------------- PRIVATE METHODS ----------------

    private void authenticateUser(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    private User createUserEntity(SignUpRequest request, Role role) {
        return User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();
    }

    private JwtAuthenticationResponse buildJwtResponse(User user) {
        return buildJwtResponse(user, jwtService.generateRefreshToken(Collections.emptyMap(), user));
    }

    private JwtAuthenticationResponse buildJwtResponse(User user, String refreshToken) {
        return JwtAuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .refreshToken(refreshToken)
                .role(user.getRole())
                .build();
    }

    private JwtAuthenticationResponse buildJwtResponse( User user, String jwt, String refreshToken) {
        return JwtAuthenticationResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .role(user.getRole())
                .build();
    }

    private boolean isRoleAssignmentAllowed(Role currentRole, Role targetRole) {
        return switch (currentRole) {
            case SUPER_ADMIN -> true;
            case ADMIN -> targetRole == Role.USER || targetRole == Role.CO_ADMIN;
            default -> false;
        };
    }
}
