package com.raven.api.notifications.services;

import com.raven.api.notifications.entity.Notification;
import com.raven.api.notifications.entity.UserNotification;
import com.raven.api.notifications.repositoryes.NotificationRepository;
import com.raven.api.notifications.repositoryes.UserNotificationRepository;
import com.raven.api.users.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void sendNotificationToUsers(Notification notification, List<User> users) {
        // Зберігаємо саму Notification
        notificationRepository.save(notification);

        // Створюємо зв'язки з користувачами та відправляємо через WebSocket
        for (User user : users) {
            UserNotification userNotification = new UserNotification();
            userNotification.setUser(user);
            userNotification.setNotification(notification);
            userNotification.setRead(false);
            userNotificationRepository.save(userNotification);

            messagingTemplate.convertAndSend("/topic/notifications", notification.getMessage());
        }
    }
}
