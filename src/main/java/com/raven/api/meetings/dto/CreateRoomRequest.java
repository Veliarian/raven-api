package com.raven.api.meetings.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {
    private String name;
    private LocalDateTime startTime;
}
