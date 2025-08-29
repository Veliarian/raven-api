package com.raven.api.notifications.mapper;

import com.raven.api.meetings.dto.RoomStartPayload;
import com.raven.api.meetings.entity.MeetingNotification;
import com.raven.api.notifications.dto.NotificationMessage;
import com.raven.api.notifications.entity.Notification;
import com.raven.api.notifications.entity.UserNotification;
import jakarta.persistence.DiscriminatorValue;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {
    public NotificationMessage toMessage(UserNotification userNotification) {
        return toMessage(userNotification.getNotification(), buildPayload(userNotification.getNotification()), userNotification.isRead());
    }

    public NotificationMessage toMessage(Notification notification, Object payload) {
        return toMessage(notification, payload, false);
    }

    public List<NotificationMessage> toMessage(List<UserNotification> userNotifications) {
        return userNotifications.stream()
                .map(this::toMessage)
                .toList();
    }

    private NotificationMessage toMessage(Notification notification, Object payload, boolean read) {
        return new NotificationMessage(
                notification.getId(),
                notification.getType(),
                notification.getCode(),
                notification.getParams(),
                notification.getCreatedAt(),
                read,
                payload
        );
    }

    private Object buildPayload(Notification notification) {
        if (notification instanceof MeetingNotification mn) {
            return new RoomStartPayload(mn.getRoomId(), mn.getRoomStatus());
        }
        return null;
    }
}
