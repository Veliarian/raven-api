package com.raven.api.lessons.entity;

import com.raven.api.files.entity.MediaFile;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lesson_files")
public class LessonFile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lesson_file_id_seq")
    @SequenceGenerator(name = "lesson_file_id_seq", sequenceName = "lesson_file_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "media_file_id", nullable = false)
    private MediaFile mediaFile;

    @Column(name = "title")
    private String title;
}
