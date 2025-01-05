package com.management.restaurant.utils;

import com.management.restaurant.DTO.restaurant.DishRequestDTO;
import com.management.restaurant.DTO.restaurant.DishResponseDTO;
import com.management.restaurant.models.restaurant.Dish;

public class DishDtoConverter {
  public static DishResponseDTO convertToResponseDTO(Dish dish) {
    DishResponseDTO dishResponseDTO = new DishResponseDTO();
    dishResponseDTO.setId(dish.getId());
    dishResponseDTO.setName(dish.getName());
    dishResponseDTO.setPrice(dish.getPrice());
    dishResponseDTO.setPopular(dish.getPopular());
    return dishResponseDTO;
  }

  public static Dish convertToEntity(DishRequestDTO dishRequestDTO) {
    Dish dish = new Dish();
    dish.setName(dishRequestDTO.getName());
    dish.setPrice(dishRequestDTO.getPrice());
    dish.setPopular(dishRequestDTO.getPopular());
    return dish;
  }
}
