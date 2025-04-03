package com.raven.api.lessons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SubjectAlreadyExistsException extends RuntimeException{
    public SubjectAlreadyExistsException(String message) {
        super(message);
    }
}
