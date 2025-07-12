package com.raven.api.meetings.dto;

import com.raven.api.meetings.entity.Room;
import com.raven.api.meetings.enums.RoomStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private Long id;
    private String name;
    private RoomStatus status;
    private LocalDateTime startTime;

    public RoomResponse(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.status = room.getStatus();
        this.startTime = room.getStartTime();
    }
}
