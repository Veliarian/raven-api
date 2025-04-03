package com.raven.api.lessons.repositories;

import com.raven.api.lessons.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findLessonsBySubjectId(Long subjectId);
    List<Lesson> findLessonsByOwnerId(Long ownerId);
}
