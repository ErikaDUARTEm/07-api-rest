package com.management.restaurant.controllers;

import com.management.restaurant.DTO.restaurant.DishResponseDTO;
import com.management.restaurant.services.DishService;
import com.management.restaurant.utils.DishDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dish")
public class DishController {
  private DishService service;

  @Autowired
  public DishController(DishService service) {
    this.service = service;
  }
  @GetMapping
  public List<DishResponseDTO> getAllDishes() {
    return service.getAllDish().stream()
      .map(DishDtoConverter::convertToResponseDTO)
      .collect(Collectors.toList());
  }

}
