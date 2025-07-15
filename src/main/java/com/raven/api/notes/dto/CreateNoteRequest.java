package com.raven.api.notes.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNoteRequest {
    private String title;
    private String content;
    private LocalDateTime creationTime;
    private LocalDateTime reminderTime;
}
