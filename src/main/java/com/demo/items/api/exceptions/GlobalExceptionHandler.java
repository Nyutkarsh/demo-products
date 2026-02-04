package com.demo.items.api.exceptions;

import com.demo.items.api.models.ApiErrorResponse;
import com.demo.items.service.ItemServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Centralized error handling this ensures consistent error responses across endpoints.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ItemServiceImpl.ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail notFound(RuntimeException ex) {
        log.info("404 Not Found: {}", ex.getMessage());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Resource Not Found");
        pd.setProperty("timestamp", Instant.now().toString());
        return pd;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse conflict(IllegalArgumentException ex) {
        log.warn("409 Conflict: {}", ex.getMessage());
        return new ApiErrorResponse(
                "CONFLICT",
                ex.getMessage(),
                Map.of(),
                Instant.now()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse validation(MethodArgumentNotValidException ex) {
        Map<String, Object> details = new LinkedHashMap<>();
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }
        details.put("errors", fieldErrors);
        log.info("400 Validation failed: {}", fieldErrors);
        return new ApiErrorResponse(
                "VALIDATION_ERROR",
                "Request validation failed",
                details,
                Instant.now()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse generic(Exception ex) {
        log.error("500 Internal error", ex);
        return new ApiErrorResponse(
                "INTERNAL_ERROR",
                "Something went wrong",
                Map.of("hint", "Check server logs for details"),
                Instant.now()
        );
    }
}

