package com.raven.api.lessons.services;

import com.raven.api.lessons.dto.LessonCreateRequest;
import com.raven.api.lessons.dto.LessonResponse;
import com.raven.api.lessons.dto.LessonUpdateRequest;
import com.raven.api.lessons.entity.Lesson;
import com.raven.api.lessons.entity.Subject;
import com.raven.api.lessons.exceptions.ForbiddenException;
import com.raven.api.lessons.exceptions.LessonNotFoundException;
import com.raven.api.lessons.repositories.LessonRepository;
import com.raven.api.users.entity.User;
import com.raven.api.users.enums.Role;
import com.raven.api.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final SubjectService subjectService;
    private final UserService userService;

    public LessonResponse toResponse(Lesson lesson) {
        return new LessonResponse(lesson);
    }

    public List<LessonResponse> toResponse(List<Lesson> lessons) {
        return lessons.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Lesson save(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public Lesson getLessonById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException("Lesson with id " + id + " not found"));
    }

    public Lesson createLesson(LessonCreateRequest request) {
        Subject subject = subjectService.getSubjectById(request.getSubjectId());

        Lesson lesson = Lesson.builder()
                .subject(subject)
                .topic(request.getTopic())
                .description(request.getDescription())
                .build();

        return save(lesson);
    }

    public Lesson updateLesson(Long id, LessonUpdateRequest request) {
        Lesson lesson = getLessonById(id);

        boolean updated = false;

        if(request.getSubjectId() != null && !lesson.getSubject().getId().equals(request.getSubjectId())) {
            Subject subject = subjectService.getSubjectById(request.getSubjectId());
            lesson.setSubject(subject);
            updated = true;
        }

        if(request.getTopic() != null && !lesson.getTopic().equals(request.getTopic())) {
            lesson.setTopic(request.getTopic());
            updated = true;
        }

        if(request.getDescription() != null && !lesson.getDescription().equals(request.getDescription())) {
            lesson.setDescription(request.getDescription());
            updated = true;
        }

        return updated ? save(lesson) : lesson;
    }

    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }

    public List<Lesson> getLessonsBySubjectId(Long subjectId) {
        return lessonRepository.findLessonsBySubjectId(subjectId);
    }

    public List<Lesson> getLessonsByUserId(Long userId) {
        User currentUser = userService.getCurrentUser();

        if(!currentUser.getId().equals(userId) && !currentUser.getRole().equals(Role.ROLE_ADMIN)) {
            throw new ForbiddenException("You are not allowed to access this resource");
        }

        return lessonRepository.findLessonsByOwnerId(currentUser.getId());
    }
}
