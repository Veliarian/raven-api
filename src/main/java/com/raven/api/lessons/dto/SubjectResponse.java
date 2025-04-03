package com.raven.api.lessons.dto;

import com.raven.api.lessons.entity.Subject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Subject response")
public class SubjectResponse {

    @Schema(description = "Subject id", example = "21")
    private Long id;

    @Schema(description = "Subject name", example = "Java")
    private String name;

    public SubjectResponse(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
    }
}
