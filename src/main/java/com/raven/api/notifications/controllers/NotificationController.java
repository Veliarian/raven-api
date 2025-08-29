package com.raven.api.notifications.controllers;

import com.raven.api.notifications.dto.NotificationMessage;
import com.raven.api.notifications.entity.UserNotification;
import com.raven.api.notifications.mapper.NotificationMapper;
import com.raven.api.notifications.services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @GetMapping
    public ResponseEntity<List<NotificationMessage>> getUnreadNotifications(){
        List<UserNotification> notifications = notificationService.getUnreadNotifications();
        return ResponseEntity.status(HttpStatus.OK).body(notificationMapper.toMessage(notifications));
    }
}
