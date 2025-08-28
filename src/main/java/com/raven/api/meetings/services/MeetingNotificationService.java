package com.raven.api.meetings.services;

import com.raven.api.meetings.dto.RoomStartNotification;
import com.raven.api.meetings.entity.Room;
import com.raven.api.meetings.entity.MeetingNotification;
import com.raven.api.meetings.enums.MeetingNotificationCode;
import com.raven.api.notifications.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MeetingNotificationService {
    private final NotificationService notificationService;

    public void sendRoomActivationNotification(Room room) {
        MeetingNotification notification = new MeetingNotification();
        notification.setRoomId(room.getId());
        notification.setRoomStatus(room.getStatus());
        notification.setParams(Map.of("roomName", room.getName()));
        notification.setCode(MeetingNotificationCode.ROOM_ACTIVATED.getCode());

        RoomStartNotification dto = RoomStartNotification.from(notification);

        notificationService.sendNotificationToUsers(notification, dto, room.getParticipants().stream().toList(), "/queue/meetings");
    }
}
