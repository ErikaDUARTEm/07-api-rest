package com.management.restaurant.utils;


import com.management.restaurant.DTO.restaurant.RestaurantResponseDTO;
import com.management.restaurant.models.restaurant.MenuRestaurant;
import com.management.restaurant.models.restaurant.Restaurant;
import org.junit.jupiter.api.Test;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantDtoConverterTest {
  @Test
  void testConvertToResponseDTO_WithMenuRestaurant() {
    Restaurant restaurant = new Restaurant();
    restaurant.setId(1L);
    restaurant.setName("Test Restaurant");

    MenuRestaurant menuRestaurant = new MenuRestaurant();
    menuRestaurant.setDescription("Test Menu");

    restaurant.setMenuRestaurant(menuRestaurant);
    RestaurantResponseDTO restaurantResponseDTO = RestaurantDtoConverter.convertToResponseDTO(restaurant);
    assertNotNull(restaurantResponseDTO.getMenuRestaurant());
    assertEquals("Test Menu", restaurantResponseDTO.getMenuRestaurant().getDescription());
  }
}