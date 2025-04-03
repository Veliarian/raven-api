package com.raven.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Registration request")
public class SignUpRequest {

    @Schema(description = "Username", example = "Jon")
    @Size(min = 2, max = 50, message = "The username must be between 3 and 50 characters long")
    @NotBlank(message = "The username cannot be blank")
    private String username;

    @Schema(description = "E-mail address", example = "jondoe@gmail.com")
    @Size(min = 7, max = 255, message = "The email address must be between 7 and 255 characters long")
    @NotBlank(message = "The email address cannot be blank")
    @Email(message = "Email address should be in the format user@example.com")
    private String email;

    @Schema(description = "Password", example = "my_1secret1_password")
    @Size(min = 8, max = 255, message = "The password must be between 8 and 255 characters long")
    private String password;
}
