package com.management.restaurant.services;

import com.management.restaurant.DTO.restaurant.RestaurantRequestDTO;
import com.management.restaurant.models.client.Client;
import com.management.restaurant.models.restaurant.Restaurant;
import com.management.restaurant.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
  public Restaurant restaurantFindById(Long id){
    return repository.findById(id).orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
  }
}
