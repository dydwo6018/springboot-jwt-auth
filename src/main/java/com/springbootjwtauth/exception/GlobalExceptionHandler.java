package com.springbootjwtauth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex) {
        Map<String, Object> responseBody = new LinkedHashMap<>();

        Map<String, String> error = new HashMap<>();
        error.put("code", ex.getErrorCode().getCode());
        error.put("message", ex.getErrorCode().getMessage());

        responseBody.put("error", error);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
