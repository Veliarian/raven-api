package com.raven.api.lessons.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Create subject request")
public class SubjectCreateRequest {

    @Schema(description = "Name", example = "Java")
    @Size(min = 1, max = 256, message = "The name must be between 1 and 256 characters long")
    @NotBlank(message = "The name cannot be blank")
    private String name;
}
