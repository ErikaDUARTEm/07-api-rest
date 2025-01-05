package com.management.restaurant.controllers;

import com.management.restaurant.DTO.client.ClientRequestDTO;
import com.management.restaurant.DTO.restaurant.MenuResponseDTO;
import com.management.restaurant.DTO.restaurant.MenuResquetDTO;
import com.management.restaurant.models.client.Client;
import com.management.restaurant.models.restaurant.MenuRestaurant;
import com.management.restaurant.models.restaurant.Restaurant;
import com.management.restaurant.services.MenuService;
import com.management.restaurant.services.RestaurantService;
import com.management.restaurant.utils.MenuDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

  private MenuService service;
  private RestaurantService restaurantService;

  @Autowired
  public MenuController(MenuService service, RestaurantService restaurantService) {
    this.service = service;
    this.restaurantService =restaurantService;
  }
  @PostMapping()
  public MenuResponseDTO addMenu(@Validated @RequestBody MenuResquetDTO menuResquetDTO) {
    Restaurant restaurant = restaurantService.restaurantFindById(menuResquetDTO.getRestaurantId());
    MenuRestaurant menuRestaurant = MenuDtoConverter.convertToEntity(menuResquetDTO, restaurant);
    MenuRestaurant createdMenu = service.createMenu(menuRestaurant);
    return MenuDtoConverter.convertToResponseDTO(createdMenu);
  }

}
