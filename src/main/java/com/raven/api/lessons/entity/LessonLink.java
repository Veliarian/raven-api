package com.raven.api.lessons.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lesson_links")
public class LessonLink {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lesson_link_id_seq")
    @SequenceGenerator(name = "lesson_link_id_seq", sequenceName = "lesson_link_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "title")
    private String title;

    @Column(name = "url", nullable = false)
    private String url;
}
