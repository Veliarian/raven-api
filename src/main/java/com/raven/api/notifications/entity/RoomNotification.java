package com.raven.api.notifications.entity;

import com.raven.api.meetings.enums.RoomStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("ROOM")
public class RoomNotification extends Notification {

    private Long roomId;
    private RoomStatus roomStatus;
}
