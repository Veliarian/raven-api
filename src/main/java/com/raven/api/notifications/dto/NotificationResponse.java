package com.raven.api.notifications.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record NotificationResponse(Long id, String code, String type, Map<String, Object> params, boolean isRead, LocalDateTime createdAt) {
}
