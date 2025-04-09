package com.raven.api.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Create user request")
public class CreateUserRequest {
    private String username;
    private String password;
}
