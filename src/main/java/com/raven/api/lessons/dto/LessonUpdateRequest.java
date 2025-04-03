package com.raven.api.lessons.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Update lesson request")
public class LessonUpdateRequest {

    @Schema(description = "Subject id", example = "34")
    private Long subjectId;

    @Schema(description = "Lesson topic", example = "Java Stream API")
    @Size(min = 1, max = 256, message = "The topic must be between 1 and 256 characters long")
    private String topic;

    @Schema(description = "Lesson description", example = "How to use Stream API with arrays...")
    private String description;
}
