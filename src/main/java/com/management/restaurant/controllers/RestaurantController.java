package com.management.restaurant.controllers;

import com.management.restaurant.DTO.restaurant.RestaurantRequestDTO;
import com.management.restaurant.DTO.restaurant.RestaurantResponseDTO;
import com.management.restaurant.models.restaurant.Restaurant;
import com.management.restaurant.services.RestaurantService;
import com.management.restaurant.utils.RestaurantDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/restaurante")
public class RestaurantController {
  private RestaurantService restaurantService;

  @Autowired
  public RestaurantController(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
  }
  @PostMapping
  public RestaurantResponseDTO createRestaurant(@Validated @RequestBody RestaurantRequestDTO restaurantRequestDTO) {
    Restaurant restaurant = RestaurantDtoConverter.convertToEntity(restaurantRequestDTO);
    Restaurant createdRestaurant = restaurantService.addRestaurant(restaurant);
    return RestaurantDtoConverter.convertToResponseDTO(createdRestaurant);
  }

  @GetMapping("/{restaurantId}")
  public ResponseEntity<RestaurantResponseDTO> getRestaurantWithMenu(@PathVariable Long restaurantId) {
    RestaurantResponseDTO restaurantWithMenu = restaurantService.getRestaurantWithMenu(restaurantId);
    return ResponseEntity.ok(restaurantWithMenu);
  }
  @DeleteMapping("/{id}")
  public ResponseEntity<String>deleteRestaurant(@PathVariable Long id) {
    try {
      restaurantService.deleteRestaurant(id);
      return ResponseEntity.ok("Restaurante eliminado con éxito.");
    } catch (EmptyResultDataAccessException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurante no encontrado.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el restaurante.");
    }
  }
}
