package com.ocs.login.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    String generateRefreshToken(Map<String, String> extraClaims, UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}
