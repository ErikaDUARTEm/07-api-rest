package com.management.restaurant.controllers;

import com.management.restaurant.DTO.restaurant.DishRequestDTO;
import com.management.restaurant.DTO.restaurant.DishResponseDTO;
import com.management.restaurant.models.restaurant.Dish;
import com.management.restaurant.services.DishService;
import com.management.restaurant.utils.DishDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
  @PostMapping
  public ResponseEntity<DishResponseDTO> createDish(@RequestBody DishRequestDTO dishRequestDTO) {
    Dish dish = service.createDish(dishRequestDTO);
    DishResponseDTO response = DishDtoConverter.convertToResponseDTO(dish);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<DishResponseDTO> updateDish(@PathVariable Long id, @RequestBody DishRequestDTO dishRequest) {
    Dish dish = service.updateDish(id, dishRequest);
    DishResponseDTO response = DishDtoConverter.convertToResponseDTO(dish);
    return ResponseEntity.ok(response);
  }
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteDish(@PathVariable Long id) {
    service.deleteDish(id);
    return ResponseEntity.ok("Plato eliminado exitosamente.");
  }
}
