package com.raven.api.notifications.controllers;

import com.raven.api.notifications.entity.Notification;
import com.raven.api.notifications.enums.NotificationType;
import com.raven.api.notifications.services.NotificationService;
import com.raven.api.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/v1/debug")
@RequiredArgsConstructor
public class WsDebugController {

    private final UserService userService;
    private final NotificationService notificationService;

    @PostMapping("/notify-me")
    public ResponseEntity<Void> notifyMe() {
        System.out.println("Notify me");
        Long userId = userService.getCurrentUser().getId(); // встановлено в STOMP CONNECT
        Notification n = new Notification();
        n.setTitle("Hello");
        n.setMessage("Це тест нотифікації лише для тебе 🙂");
        n.setType(NotificationType.SYSTEM);
        notificationService.sendToUser(userId, n);
        return ResponseEntity.ok().build();
    }
}
