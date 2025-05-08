package com.ssafy.pjtaserver.exception;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomJWTException.class)
    protected ResponseEntity<?> handleJWTException(CustomJWTException e) {
        return ResponseEntity.ok().body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(MessagingException.class)
    protected ResponseEntity<?> handleMessagingException(MessagingException e) {
        return ResponseEntity.ok().body(Map.of("message", e.getMessage()));
    }
}
