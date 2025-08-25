package com.raven.api.notifications.entity;

import com.raven.api.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_notifications")
public class UserNotification {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_notification_id_seq")
    @SequenceGenerator(name = "ser_notification_id_seq", sequenceName = "ser_notification_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Notification notification;

    private boolean isRead;

    private LocalDateTime createdAt = LocalDateTime.now();
}
