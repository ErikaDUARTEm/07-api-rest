package com.management.restaurant.services;

import com.management.restaurant.models.restaurant.Restaurant;
import com.management.restaurant.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
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

  public Restaurant getRestaurantWithMenu(Long restaurantId) {
    Optional<Restaurant> optionalRestaurant = repository.findMenuByRestaurant(restaurantId);
    if (optionalRestaurant.isPresent()) {
     return optionalRestaurant.get();
    } else {
      return repository.findById(restaurantId).orElse(null);
    }
  }

  public void deleteRestaurant(Long id){
    if (!repository.existsById(id)) {
      throw new EmptyResultDataAccessException("Restaurante no encontrado con ID: " + id, 1);
    }
    repository.deleteById(id);
  }
}
