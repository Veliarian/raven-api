package com.raven.api.meetings.services;

import com.raven.api.meetings.dto.RoomStartNotification;
import com.raven.api.meetings.entity.Room;
import com.raven.api.meetings.entity.RoomNotification;
import com.raven.api.meetings.enums.MeetingNotificationCode;
import com.raven.api.notifications.enums.TargetType;
import com.raven.api.notifications.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingNotificationService {
    private final NotificationService notificationService;

    public void sendMeetingActivatedNotification(Room room) {
        RoomNotification notification = new RoomNotification();
        notification.setRoomId(room.getId());
        notification.setRoomStatus(room.getStatus());
        notification.setCode(MeetingNotificationCode.ROOM_ACTIVATED.getCode());

        RoomStartNotification dto = RoomStartNotification.from(notification);

        notificationService.sendNotificationToAllUsers(notification, dto);
    }
}
