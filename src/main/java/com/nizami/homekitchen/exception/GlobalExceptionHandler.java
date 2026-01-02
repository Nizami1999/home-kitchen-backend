package com.nizami.homekitchen.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.exc.UnrecognizedPropertyException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Bean validation errors (@NotBlank, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("message", "Validation failed");
        response.put("errors", errors);

        return response;
    }

    // JSON parsing / unknown fields
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleJsonParseError(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON request: {}", ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);

        Throwable cause = ex.getCause();
        if (cause instanceof UnrecognizedPropertyException unrecognized) {
            response.put(
                    "message",
                    "Unknown field '" + unrecognized.getPropertyName() + "' in request body"
            );
        } else {
            response.put("message", "Malformed JSON request");
        }

        return response;
    }

    // Explicit ResponseStatusException (404, 400, etc.)
    @ExceptionHandler(ResponseStatusException.class)
    public Map<String, Object> handleResponseStatusException(ResponseStatusException ex) {
        log.warn("ResponseStatusException: {} - {}", ex.getStatusCode(), ex.getReason(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", ex.getStatusCode().value());
        response.put("message", ex.getReason());

        return response;
    }

    // Database constraint violations (foreign keys, unique constraints)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Database constraint violation: {}", ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 409);
        response.put("message", "Operation failed due to database constraint");
        response.put("details", ex.getMostSpecificCause().getMessage());

        return response;
    }

    // Catch-all for any unhandled exceptions
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleAllUnhandledExceptions(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 500);
        response.put("message", "Internal server error");

        return response;
    }
}
