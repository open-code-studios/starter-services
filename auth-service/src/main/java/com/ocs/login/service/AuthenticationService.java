package com.ocs.login.service;

import com.ocs.login.dto.JwtAuthenticationResponse;
import com.ocs.login.dto.RefreshTokenRequest;
import com.ocs.login.dto.SignUpRequest;
import com.ocs.login.dto.SignInRequest;
import com.ocs.login.entity.User;

public interface AuthenticationService {

    User signup(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signin(SignInRequest signinRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
