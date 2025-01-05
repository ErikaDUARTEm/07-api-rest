package com.management.restaurant.controllers;

import com.management.restaurant.services.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dish")
public class DishController {
  private DishService service;

  @Autowired
  public DishController(DishService service) {
    this.service = service;
  }

}
