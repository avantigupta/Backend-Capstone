package com.backend.lms.exception;

import com.backend.lms.dto.ResponseDto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    @ExceptionHandler(EntityConstraintViolationException.class)
    public ResponseEntity<ResponseDTO> handleEntityConstraintViolationException(EntityConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ResponseDTO(String.valueOf(HttpStatus.CONFLICT.value()), ex.getMessage()));
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<ResponseDTO> handleBookAlreadyExistsException(BookAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ResponseDTO(String.valueOf(HttpStatus.CONFLICT.value()), ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(String.valueOf(HttpStatus.NOT_FOUND.value()), ex.getMessage()));
    }

    @ExceptionHandler(UserHasIssuedBooksException.class)
    public ResponseEntity<ResponseDTO> handleUserHasIssuedBooksException(UserHasIssuedBooksException ex){
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                .body(new ResponseDTO(String.valueOf(HttpStatus.PRECONDITION_FAILED.value()), ex.getMessage()));
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<ResponseDTO> handleMethodNotAllowed(MethodNotAllowedException ex){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ResponseDTO(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()), ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleUsernameNotFoundException(UsernameNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(String.valueOf(HttpStatus.NOT_FOUND.value()), ex.getMessage()));
    }

}