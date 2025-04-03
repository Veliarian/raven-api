package com.raven.api.lessons.controllers;

import com.raven.api.lessons.dto.LessonCreateRequest;
import com.raven.api.lessons.dto.LessonResponse;
import com.raven.api.lessons.dto.LessonUpdateRequest;
import com.raven.api.lessons.entity.Lesson;
import com.raven.api.lessons.services.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/lessons")
@RequiredArgsConstructor
@Tag(name = "Lessons")
public class LessonController {

    private final LessonService lessonService;

    @Operation(summary = "Get all lessons")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LessonResponse>> getAllLessons() {
        List<Lesson> lessons = lessonService.getAllLessons();
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.toResponse(lessons));
    }

    @Operation(summary = "Create a new lesson")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<LessonResponse> createLesson(@RequestBody @Valid LessonCreateRequest request) {
        Lesson createdLesson = lessonService.createLesson(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.toResponse(createdLesson));
    }

    @Operation(summary = "Get lesson by id")
    @GetMapping("/{id}")
    public ResponseEntity<LessonResponse> getLessonById(@PathVariable Long id) {
        Lesson lesson = lessonService.getLessonById(id);
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.toResponse(lesson));
    }

    @Operation(summary = "Update lesson")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<LessonResponse> updateLesson(@PathVariable Long id, @RequestBody @Valid LessonUpdateRequest request) {
        Lesson updatedLesson = lessonService.updateLesson(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.toResponse(updatedLesson));
    }

    @Operation(summary = "Delete lesson")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Get lesson by subject id")
    @GetMapping("/subject/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<LessonResponse>> getLessonBySubject(@PathVariable Long id) {
        List<Lesson> lessons = lessonService.getLessonsBySubjectId(id);
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.toResponse(lessons));
    }

    @Operation(summary = "Get lesson by user id")
    @GetMapping("/user/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<LessonResponse>> getLessonByUser(@PathVariable Long id) {
        List<Lesson> lessons = lessonService.getLessonsByUserId(id);
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.toResponse(lessons));
    }
}
