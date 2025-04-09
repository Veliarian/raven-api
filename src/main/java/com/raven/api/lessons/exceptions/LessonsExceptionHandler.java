package com.raven.api.lessons.exceptions;

import com.raven.api.tools.exceptions.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class LessonsExceptionHandler {

    private ResponseEntity<Map<String, String>> response(String error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", error));
    }

    @ExceptionHandler(LessonNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLessonNotFoundException(LessonNotFoundException ex) {
        return response(ex.getMessage());
    }

    @ExceptionHandler(SubjectAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleSubjectAlreadyExistsException(SubjectAlreadyExistsException ex) {
        return response(ex.getMessage());
    }

    @ExceptionHandler(SubjectNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSubjectNotFoundException(SubjectNotFoundException ex) {
        return response(ex.getMessage());
    }
}
