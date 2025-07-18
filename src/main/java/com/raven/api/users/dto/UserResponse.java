package com.raven.api.users.dto;

import com.raven.api.users.entity.User;
import com.raven.api.users.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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
    private Role role;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Patronymic", example = "James")
    private String patronymic;

    @Schema(description = "Biography", example = "Mathematics teacher with 5+ years of experience")
    private String bio;

    @Schema(description = "Profile picture", example = "avatar.jpg")
    private String profilePicture;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.emailVerified = user.getEmailVerified();
        this.role = user.getRole();

        if(user.getUserProfile() != null) {
            this.firstName = user.getUserProfile().getFirstname();
            this.lastName = user.getUserProfile().getLastname();
            this.patronymic = user.getUserProfile().getPatronymic();
            this.bio = user.getUserProfile().getBio();
        }

        if(user.getProfilePicture() != null) {
            this.profilePicture = user.getProfilePicture().getImageName();
        }
    }
}
