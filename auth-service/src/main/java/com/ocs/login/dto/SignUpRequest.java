package com.ocs.login.dto;

import com.ocs.login.validation.unique_email.UniqueEmail;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotBlank(message = "{not.blank}")
    @Size(min = 2, max = 50, message = "{firstname.size}")
    private String firstName;

    @NotBlank(message = "{not.blank}")
    @Size(min = 2, max = 50, message = "{lastname.size}")
    private String lastName;

    @NotBlank(message = "{not.blank}")
    @Email(message = "{email.invalid}")
    @UniqueEmail(message = "{email.exists}")
    private String email;

    @NotBlank(message = "{not.blank}")
    @Size(min = 8, max = 100, message = "{password.size}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "{password.pattern}")
    private String password;
}