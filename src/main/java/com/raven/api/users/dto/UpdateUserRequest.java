package com.raven.api.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Update user data request")
public class UpdateUserRequest {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String patronymic;
}
