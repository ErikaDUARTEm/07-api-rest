package com.management.restaurant.utils;

import com.management.restaurant.DTO.restaurant.MenuResponseDTO;
import com.management.restaurant.DTO.restaurant.MenuResquetDTO;
import com.management.restaurant.models.restaurant.MenuRestaurant;
import com.management.restaurant.models.restaurant.Restaurant;

import java.util.stream.Collectors;

public class MenuDtoConverter {

  public static MenuResponseDTO convertToResponseDTO(MenuRestaurant menu) {
    MenuResponseDTO menuResponseDTO = new MenuResponseDTO();
    menuResponseDTO.setId(menu.getIdMenu());
    menuResponseDTO.setDescription(menu.getDescription());
    menuResponseDTO.setDishes(menu.getDishes().stream()
      .map(DishDtoConverter::convertToResponseDTO)
      .collect(Collectors.toList()));
    return menuResponseDTO;
  }

  public static MenuRestaurant convertToEntity(MenuResquetDTO menuRequestDTO, Restaurant restaurant) {
    MenuRestaurant menu = new MenuRestaurant();
    menu.setDescription(menuRequestDTO.getDescription());
    menu.setRestaurant(restaurant);
    menu.setDishes(menuRequestDTO.getDishes().stream()
      .map(DishDtoConverter::convertToEntity)
      .collect(Collectors.toList()));
    return menu;
  }

}
