package com.raven.api.notifications.entity;

import com.raven.api.notifications.enums.NotificationType;
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
@Table(name = "notifications")
public class Notification {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_id_seq")
    @SequenceGenerator(name = "notification_id_seq", sequenceName = "notification_id_seq", allocationSize = 1)
    private Long id;

    private String title;
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type; // LESSON, CHAT, CALENDAR, SYSTEM

    private boolean read = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Якщо null -> для всіх
    private Long targetUserId;

    // Якщо null -> не група
    private String targetGroup;
}