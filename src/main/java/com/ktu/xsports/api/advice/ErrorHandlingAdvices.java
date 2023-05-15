package com.ktu.xsports.api.advice;

import com.ktu.xsports.api.advice.exceptions.AlreadyExistsException;
import com.ktu.xsports.api.advice.exceptions.ImageUploadException;
import com.ktu.xsports.api.advice.exceptions.NotAnImageException;
import com.ktu.xsports.api.advice.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandlingAdvices {
    private static final String ERROR = "error";

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleServiceException(ServiceException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(ERROR, exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach(error ->
            errors.add(error.getDefaultMessage())
        );
        return ResponseEntity
                .badRequest()
                .body(Map.of(ERROR, errors));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleAlreadyExistsExceptions(AlreadyExistsException exception) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(ERROR, exception.getMessage()));
    }

    @ExceptionHandler(NotAnImageException.class)
    public ResponseEntity<Map<String, String>> handleNotAnImageException(NotAnImageException exception) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(ERROR, exception.getMessage()));
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<Map<String, String>> handleImageUploadException(ImageUploadException exception) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(ERROR, exception.getMessage()));
    }
}
