package com.management.restaurant.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DishAlreadyExistsException extends RuntimeException {
  public DishAlreadyExistsException(String message) {
    super(message);
  }
}
