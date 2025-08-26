package com.raven.api.meetings.entity;

import com.raven.api.meetings.enums.RoomStatus;
import com.raven.api.notifications.entity.Notification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("ROOM")
public class RoomNotification extends Notification {

    @Column(name = "room_id")
    private Long roomId;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_status")
    private RoomStatus roomStatus;
}
