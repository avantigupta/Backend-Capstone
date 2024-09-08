package com.backend.lms.exception;

import com.backend.lms.dto.ResponseDto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ResponseDTO> handleResourceConflict(ResourceConflictException ex) {
        // Return a ResponseDTO with conflict status and the exception message
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ResponseDTO(String.valueOf(HttpStatus.CONFLICT.value()), ex.getMessage()));
    }


}