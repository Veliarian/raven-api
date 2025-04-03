package com.raven.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Login request")
public class SignInRequest {

    @Schema(description = "Username", example = "Jon")
    @Size(min = 3, max = 50, message = "The username must be between 3 and 50 characters long")
    @NotBlank(message = "The username cannot be blank")
    private String username;

    @Schema(description = "Password", example = "my_1secret1_password")
    @Size(min = 8, max = 255, message = "The password must be between 8 and 255 characters long")
    @NotBlank(message = "The password cannot be blank")
    private String password;
}
