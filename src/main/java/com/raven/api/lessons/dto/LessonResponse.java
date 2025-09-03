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

    @Schema(description = "Lesson content", example = "How to use Stream API with arrays...")
    private String content;

    @Schema(description = "Subject", example = "Java")
    private String subject;

    public LessonResponse(Lesson lesson) {
        this.id = lesson.getId();
        this.topic = lesson.getTopic();
        this.content = lesson.getContent();
        this.subject = lesson.getSubject().getName();
    }
}
