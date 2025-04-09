package com.raven.api.users.dto;

import com.raven.api.users.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Update user role")
public class UpdateRoleRequest {
    private Role role;
}
