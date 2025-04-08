package com.raven.api.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User response")
public class UserResponse {

    @Schema(description = "User id", example = "73")
    private Long id;

    @Schema(description = "Username", example = "JohnDoe")
    private String username;

    @Schema(description = "Email", example = "jd@example.com")
    private String email;

    @Schema(description = "Email verified", example = "true")
    private Boolean emailVerified;

    @Schema(description = "User role", example = "ROLE_USER")
    private String role;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Patronymic", example = "James")
    private String patronymic;
}
