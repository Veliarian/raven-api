package com.raven.api.notes.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReminderTimeRequest {
    private LocalDateTime reminderTime;
}
