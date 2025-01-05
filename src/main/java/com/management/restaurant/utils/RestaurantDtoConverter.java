package com.management.restaurant.utils;

import com.management.restaurant.DTO.restaurant.RestaurantRequestDTO;
import com.management.restaurant.DTO.restaurant.RestaurantResponseDTO;
import com.management.restaurant.models.restaurant.Restaurant;


public class RestaurantDtoConverter {
  public static RestaurantResponseDTO convertToResponseDTO(Restaurant restaurant) {
    RestaurantResponseDTO restaurantResponseDTO = new RestaurantResponseDTO();
    restaurantResponseDTO.setId(restaurant.getId());
    restaurantResponseDTO.setName(restaurant.getName());
    restaurantResponseDTO.setAddress(restaurant.getAddress());
    restaurantResponseDTO.setPhoneNumber(restaurant.getPhoneNumber());
    restaurantResponseDTO.setOpeningHours(restaurant.getOpeningHours());
    restaurantResponseDTO.setClosingHours(restaurant.getClosingHours());
    if (restaurant.getMenuRestaurant() != null) {
      restaurantResponseDTO.setMenuRestaurant(MenuDtoConverter.convertToResponseDTO(restaurant.getMenuRestaurant()));
    }
    return restaurantResponseDTO;
  }

    public static Restaurant convertToEntity(RestaurantRequestDTO restaurantRequestDTO) {
      Restaurant restaurant = new Restaurant();
      restaurant.setName(restaurantRequestDTO.getName());
      restaurant.setAddress(restaurantRequestDTO.getAddress());
      restaurant.setPhoneNumber(restaurantRequestDTO.getPhoneNumber());
      restaurant.setOpeningHours(restaurantRequestDTO.getOpeningHours());
      restaurant.setClosingHours(restaurantRequestDTO.getClosingHours());
      return restaurant;
    }

}
