package com.management.restaurant.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DishAlreadyExistsException.class)
  public ResponseEntity<Map<String, Object>> handleDishAlreadyExists(DishAlreadyExistsException ex) {
    Map<String, Object> errorBody = new HashMap<>();
    errorBody.put("status", HttpStatus.CONFLICT.value());
    errorBody.put("error", ex.getMessage());
    errorBody.put("timestamp", LocalDateTime.now());

    return new ResponseEntity<>(errorBody, HttpStatus.CONFLICT);
  }
}