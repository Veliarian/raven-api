package com.raven.api.lessons.dto;

import com.raven.api.lessons.entity.Lesson;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Lesson response")
public class LessonResponse {

    @Schema(description = "Lesson id", example = "73")
    private Long id;

    @Schema(description = "Lesson topic", example = "Java Stream API")
    private String topic;

    @Schema(description = "Lesson description", example = "How to use Stream API with arrays...")
    private String description;

    @Schema(description = "Subject", example = "{id: 5, name: 'Java'}")
    private SubjectResponse subjectResponse;

    public LessonResponse(Lesson lesson) {
        this.id = lesson.getId();
        this.topic = lesson.getTopic();
        this.description = lesson.getDescription();
        this.subjectResponse = new SubjectResponse(lesson.getSubject());
    }
}
