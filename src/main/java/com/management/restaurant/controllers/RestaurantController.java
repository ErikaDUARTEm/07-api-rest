package com.management.restaurant.controllers;

import com.management.restaurant.DTO.restaurant.MenuResponseDTO;
import com.management.restaurant.DTO.restaurant.MenuResquetDTO;
import com.management.restaurant.DTO.restaurant.RestaurantRequestDTO;
import com.management.restaurant.DTO.restaurant.RestaurantResponseDTO;
import com.management.restaurant.models.restaurant.MenuRestaurant;
import com.management.restaurant.models.restaurant.Restaurant;
import com.management.restaurant.services.RestaurantService;
import com.management.restaurant.utils.MenuDtoConverter;
import com.management.restaurant.utils.RestaurantDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
