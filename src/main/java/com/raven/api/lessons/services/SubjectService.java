package com.raven.api.lessons.services;

import com.raven.api.lessons.dto.SubjectCreateRequest;
import com.raven.api.lessons.dto.SubjectResponse;
import com.raven.api.lessons.dto.SubjectUpdateRequest;
import com.raven.api.lessons.entity.Subject;
import com.raven.api.lessons.exceptions.SubjectAlreadyExistsException;
import com.raven.api.lessons.exceptions.SubjectNotFoundException;
import com.raven.api.lessons.repositories.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository repository;

    public SubjectResponse toResponse(Subject subject) {
        return new SubjectResponse(subject);
    }

    public List<SubjectResponse> toResponse(List<Subject> subjects) {
        return subjects.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Subject save(Subject subject) {
        return repository.save(subject);
    }

    public Subject create(SubjectCreateRequest request) {
        if(repository.existsByName(request.getName())){
            throw new SubjectAlreadyExistsException("Subject " + request.getName() + " already exists");
        }

        Subject subject = Subject.builder()
                .name(request.getName())
                .build();

        return repository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return repository.findAll();
    }

    public Subject getSubjectById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Subject id is null");
        };

        return repository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException("Subject with id " + id + " not found"));
    }

    public Subject updateSubject(Long id, SubjectUpdateRequest request) {
        Subject subject = getSubjectById(id);
        boolean updated = false;

        if(request.getName() != null && !request.getName().equals(subject.getName())) {
            subject.setName(request.getName());
            updated = true;
        }

        return updated ? save(subject) : subject;
    }

    public void deleteSubject(Long id) {
        repository.deleteById(id);
    }
}
