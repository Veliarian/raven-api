package com.raven.api.notifications.services;

import com.raven.api.notifications.dto.NotificationMessage;
import com.raven.api.notifications.entity.Notification;
import com.raven.api.notifications.entity.UserNotification;
import com.raven.api.notifications.mapper.NotificationMapper;
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
    private final NotificationMapper notificationMapper;
    private final UserService userService;

    /*
     * Save a notification to db and send a message to users
     * @param notification - notification to save
     * @param message - message to send
     * @param users - users to send message
     * @param destination - destination for send a message
     */
    @Transactional
    public void sendNotificationToUsers(Notification notification, Object payload, List<User> users, String destination) {

        notification = notificationRepository.save(notification);

        NotificationMessage message = notificationMapper.toMessage(notification, payload);

        for (User user : users) {
            UserNotification userNotification = new UserNotification();
            userNotification.setUser(user);
            userNotification.setNotification(notification);
            userNotification.setRead(false);
            userNotificationRepository.save(userNotification);

            messagingTemplate.convertAndSendToUser(user.getUsername(), destination, message);
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

    // Get all unread notifications for the current user
    public List<UserNotification> getUnreadNotifications() {
        User user = userService.getCurrentUser();
        return userNotificationRepository.findByUser_IdAndIsReadIsFalseOrderByNotification_CreatedAtDesc(user.getId());
    }
}
