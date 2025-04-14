package com.ocs.login.dto;

import com.ocs.login.validation.email_exists.EmailNotExists;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequest {

    @NotBlank(message = "{not.blank}")
    @Email(message = "{email.invalid}")
    @EmailNotExists(message = "{email.not.exists}")
    private String email;

    @NotBlank(message = "{not.blank}")
    private String password;
}
