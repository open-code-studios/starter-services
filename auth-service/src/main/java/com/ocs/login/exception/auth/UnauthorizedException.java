package com.ocs.login.exception.auth;

import com.ocs.login.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException() {
        super("UNAUTHORIZED", "error.auth.unauthorized");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
