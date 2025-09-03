package com.raven.api.lessons.entity;

import com.raven.api.lessons.enums.LessonAccessType;
import com.raven.api.users.entity.Group;
import com.raven.api.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lesson_access")
public class LessonAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lesson_access_id_seq")
    @SequenceGenerator(name = "lesson_access_id_seq", sequenceName = "lesson_access_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_type", nullable = false)
    private LessonAccessType accessType;
}
