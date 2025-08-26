package com.raven.api.notifications.dto;

import com.raven.api.notifications.entity.Notification;

public record NotificationResponse(Long id, String code, boolean isRead) {
}
