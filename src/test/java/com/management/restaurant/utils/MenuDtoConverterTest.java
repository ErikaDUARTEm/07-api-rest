package com.management.restaurant.utils;

import com.management.restaurant.DTO.restaurant.DishRequestDTO;
import com.management.restaurant.DTO.restaurant.MenuResquetDTO;
import com.management.restaurant.models.restaurant.Dish;
import com.management.restaurant.models.restaurant.MenuRestaurant;
import com.management.restaurant.models.restaurant.Restaurant;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuDtoConverterTest {


  @Test
  void convertToEntity() {
    MenuResquetDTO menuRequestDTO = new MenuResquetDTO();
    menuRequestDTO.setDescription("Delicious menu");
    DishRequestDTO dishRequestDTO = new DishRequestDTO();
    dishRequestDTO.setName("Sushi");
    dishRequestDTO.setPrice(15.99);
    menuRequestDTO.setDishes(Collections.singletonList(dishRequestDTO));
    Restaurant restaurant = new Restaurant(); restaurant.setName("Sushi Palace");
    MenuRestaurant menu = MenuDtoConverter.convertToEntity(menuRequestDTO, restaurant);
    assertNotNull(menu);
    assertEquals(1, menu.getDishes().size());
    Dish dish = menu.getDishes().get(0);
    assertNotNull(dish);
    assertEquals("Sushi", dish.getName());
    assertEquals(15.99, dish.getPrice());
    assertEquals(menu, dish.getMenuRestaurant());
  }
}