package com.raven.api.meetings.dto;

import com.raven.api.meetings.entity.Room;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private Long id;
    private String name;

    public RoomResponse(Room room) {
        this.id = room.getId();
        this.name = room.getName();
    }
}
