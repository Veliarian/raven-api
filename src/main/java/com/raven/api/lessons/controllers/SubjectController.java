package com.raven.api.lessons.controllers;

import com.raven.api.lessons.dto.SubjectCreateRequest;
import com.raven.api.lessons.dto.SubjectResponse;
import com.raven.api.lessons.dto.SubjectUpdateRequest;
import com.raven.api.lessons.entity.Subject;
import com.raven.api.lessons.services.SubjectService;
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
@RequiredArgsConstructor
@RequestMapping("/v1/subjects")
@Tag(name = "Subjects")
public class SubjectController {

    private final SubjectService subjectService;

    @Operation(summary = "Get all subjects")
    @GetMapping
    public ResponseEntity<List<SubjectResponse>> getAllSubjects() {
        List<Subject> subjects = subjectService.getAllSubjects();
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.toResponse(subjects));
    }

    @Operation(summary = "Create a new subject")
    @PostMapping
    public ResponseEntity<SubjectResponse> createSubject(@RequestBody @Valid SubjectCreateRequest request) {
        Subject subject = subjectService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.toResponse(subject));
    }

    @Operation(summary = "Get subject by id")
    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> getSubjectById(@PathVariable Long id) {
        Subject subject = subjectService.getSubjectById(id);
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.toResponse(subject));
    }

    @Operation(summary = "Update subject")
    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponse> updateSubject(@PathVariable Long id, @RequestBody @Valid SubjectUpdateRequest request) {
        Subject subject = subjectService.updateSubject(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.toResponse(subject));
    }

    @Operation(summary = "Delete subject")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
