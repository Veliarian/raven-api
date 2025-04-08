package com.raven.api.lessons.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Create lesson request")
public class LessonCreateRequest {

    @Schema(description = "Subject Id", example = "33")
    @NotNull(message = "The subject id cannot be null")
    private Long subjectId;

    @Schema(description = "Topic", example = "Java Stream API")
    @Size(min = 1, max = 256, message = "The topic must be between 1 and 256 characters long")
    @NotBlank(message = "The topic cannot be blank")
    private String topic;

    @Schema(description = "Description", example = "How to use Stream API with arrays...")
    private String description;
}
