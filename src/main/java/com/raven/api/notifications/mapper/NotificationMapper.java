package com.raven.api.notifications.mapper;

import com.raven.api.notifications.dto.NotificationResponse;
import com.raven.api.notifications.entity.Notification;
import com.raven.api.notifications.entity.UserNotification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {
    public NotificationResponse toResponse(UserNotification userNotification) {
        if (userNotification == null) return null;
        if (userNotification.getNotification() == null) return null;

        Notification notification = userNotification.getNotification();

        return new NotificationResponse(
                notification.getId(),
                notification.getCode(),
                notification.getType(),
                notification.getParams(),
                userNotification.isRead(),
                userNotification.getCreatedAt()
        );
    }

    public List<NotificationResponse> toResponse(List<UserNotification> notifications) {
        return notifications.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
