package com.management.restaurant.services;

import com.management.restaurant.DTO.restaurant.RestaurantResponseDTO;
import com.management.restaurant.models.restaurant.Restaurant;
import com.management.restaurant.repositories.RestaurantRepository;
import com.management.restaurant.utils.RestaurantDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
  private RestaurantRepository repository;

  @Autowired
  public RestaurantService(RestaurantRepository repository) {
    this.repository = repository;
  }
  public Restaurant addRestaurant(Restaurant restaurant){
   return repository.save(restaurant);
  }
  public RestaurantResponseDTO getRestaurantWithMenu(Long restaurantId) {
    Optional<Restaurant> optionalRestaurant = repository.findMenuByRestaurant(restaurantId);
    if (optionalRestaurant.isPresent()) {
      Restaurant restaurant = optionalRestaurant.get();
      return RestaurantDtoConverter.convertToResponseDTO(restaurant);
    } else {
      return repository.findById(restaurantId)
        .map(RestaurantDtoConverter::convertToResponseDTO)
        .orElse(null);
    }
  }

  public void deleteRestaurant(Long id){
    if (!repository.existsById(id)) {
      throw new EmptyResultDataAccessException("Restaurante no encontrado con ID: " + id, 1);
    }
    repository.deleteById(id);
  }
}
