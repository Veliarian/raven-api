package com.raven.api.meetings.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MeetingTokenCreateException extends RuntimeException{
    public MeetingTokenCreateException(String message) {
        super(message);
    }
}
