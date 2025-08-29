package com.raven.api.notifications.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record NotificationMessage(
        Long id,
        String type,
        String code,
        Map<String, Object> params,
        LocalDateTime createdAt,
        boolean isRead,
        Object payload
) {}
