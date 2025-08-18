package com.raven.api.notifications.controllers;

import com.raven.api.notifications.entity.Notification;
import com.raven.api.notifications.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/unread/{userId}")
    public List<Notification> getUnread(@PathVariable Long userId) {
        return notificationService.getUnread(userId);
    }

    @PostMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }
}

