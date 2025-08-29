package com.raven.api.notifications.repositoryes;

import com.raven.api.notifications.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    List<UserNotification> findByUser_IdAndIsReadIsFalseOrderByNotification_CreatedAtDesc(Long userId);
}
