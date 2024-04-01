package com.nhandn.backend.exception;

import com.nhandn.backend.dto.ErrorDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ErrorDetails.builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AuthenticationException.class })
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleAuthenticationException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(ErrorDetails.builder()
                .timestamp(new Date())
                .message(String.format("Access denied message here: %s", ex.getMessage()))
                .details(request.getDescription(false))
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        try {
            List<String> messages = ex.getConstraintViolations()
                    .stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
            return new ResponseEntity<>(ErrorDetails.builder()
                    .timestamp(new Date())
                    .message(String.join(",", messages))
                    .details(request.getRequestURI())
                    .build(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDetails.builder()
                    .timestamp(new Date())
                    .message(e.getMessage())
                    .details(request.getRequestURI())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);}
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ErrorDetails.builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}