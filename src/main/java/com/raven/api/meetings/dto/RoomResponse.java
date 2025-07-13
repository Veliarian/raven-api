package com.raven.api.meetings.dto;

import com.raven.api.meetings.entity.Room;
import com.raven.api.meetings.enums.RoomStatus;
import com.raven.api.users.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<Long> participantIds;

    public RoomResponse(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.status = room.getStatus();
        this.startTime = room.getStartTime();
        this.participantIds = room.getParticipants().stream().map(User::getId).toList();
    }
}
