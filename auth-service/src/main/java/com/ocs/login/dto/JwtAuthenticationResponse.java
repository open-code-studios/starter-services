package com.ocs.login.dto;

import com.ocs.login.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtAuthenticationResponse {

    private String token;

    private String refreshToken;

    private Role role;
}
