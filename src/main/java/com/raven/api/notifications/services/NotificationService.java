package com.raven.api.notifications.services;

import com.raven.api.notifications.entity.Notification;
import com.raven.api.notifications.repositoryes.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    public Notification sendToUser(Long userId, Notification notification) {
        notification.setTargetUserId(userId); // якщо у тебе Long id
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        Notification saved = notificationRepository.save(notification);

        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/notifications",
                saved
        );
        return saved;
    }

    public Notification sendToAll(Notification notification) {
        Notification saved = notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/notifications", saved);
        return saved;
    }

    public Notification sendToGroup(String group, Notification notification) {
        notification.setTargetGroup(group);
        notification.setCreatedAt(LocalDateTime.now());
        Notification saved = notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/group/" + group, saved);
        return saved;
    }

    public List<Notification> getUnread(Long userId) {
        return notificationRepository.findByTargetUserIdAndReadFalse(userId);
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }
}
