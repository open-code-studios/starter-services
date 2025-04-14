package com.ocs.login.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ocs.login.exception.ApiException;
import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String code;
    private String message;
    private String path;
    private List<FieldError> fieldErrors;

    @Data
    public static class FieldError {
        private String field;
        private String code;
        private String message;
        private Object rejectedValue;

        public FieldError(String field, String code, String message, Object rejectedValue) {
            this.field = field;
            this.code = code;
            this.message = message;
            this.rejectedValue = rejectedValue;
        }
    }

    public ErrorResponse(int status, String error, String code, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
        this.path = path;
    }

    public static ErrorResponse createFromApiException(ApiException ex, MessageSource messageSource, WebRequest request) {
        Locale locale = LocaleContextHolder.getLocale();
        String resolvedMessage = messageSource.getMessage(ex.getMessage(), null, ex.getMessage(), locale);

        return new ErrorResponse(ex.getHttpStatus().value(), ex.getHttpStatus().getReasonPhrase(), ex.getErrorCode(), resolvedMessage, request.getDescription(false).replace("uri=", ""));
    }
}