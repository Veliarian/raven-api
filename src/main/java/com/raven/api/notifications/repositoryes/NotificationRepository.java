package com.raven.api.notifications.repositoryes;

import com.raven.api.notifications.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByTargetUserIdAndReadFalse(Long userId);
}
