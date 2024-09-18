package com.backend.lms.exception;

import org.springframework.http.HttpStatus;

public class EntityConstraintViolationException extends RuntimeException{

    private final HttpStatus status;

    public EntityConstraintViolationException(String message) {
        super(message);
        this.status = HttpStatus.CONFLICT;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
