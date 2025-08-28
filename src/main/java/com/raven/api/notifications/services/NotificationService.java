package com.raven.api.notifications.services;

import com.raven.api.notifications.entity.Notification;
import com.raven.api.notifications.entity.UserNotification;
import com.raven.api.notifications.repositoryes.NotificationRepository;
import com.raven.api.notifications.repositoryes.UserNotificationRepository;
import com.raven.api.users.entity.User;
import com.raven.api.users.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final SimpUserRegistry simpUserRegistry;

    @Transactional
    public void sendNotificationToUsers(Notification notification, Object dto, List<User> users, String destination) {
        // Зберігаємо саму Notification
        notificationRepository.save(notification);

        // Створюємо зв'язки з користувачами та відправляємо через WebSocket
        for (User user : users) {
            UserNotification userNotification = new UserNotification();
            userNotification.setUser(user);
            userNotification.setNotification(notification);
            userNotification.setRead(false);
            userNotificationRepository.save(userNotification);

            messagingTemplate.convertAndSendToUser(user.getUsername(),destination, dto);
        }
    }

    @Transactional
    public void sendNotificationToAllUsers(Notification notification, Object dto) {
        notificationRepository.save(notification);

        List<User> users = userService.getAll();
        for (User user : users) {
            UserNotification userNotification = new UserNotification();
            userNotification.setUser(user);
            userNotification.setNotification(notification);
            userNotification.setRead(false);
            userNotificationRepository.save(userNotification);
        }

        messagingTemplate.convertAndSend("/topic/notifications", dto);
    }

    public List<UserNotification> getUnreadNotifications() {
        User user = userService.getCurrentUser();
        return userNotificationRepository.findByUser_IdAndIsReadIsFalseOrderByCreatedAtDesc(user.getId());
    }
}
