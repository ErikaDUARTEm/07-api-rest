package com.management.restaurant.services;

import com.management.restaurant.models.restaurant.Restaurant;
import com.management.restaurant.repositories.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  @Transactional
  public Restaurant updateRestaurant(Long id, Restaurant updatedData) {
    Restaurant restaurant = repository.findRestaurantById(id);

    if (restaurant == null) {
      throw new EntityNotFoundException("Restaurante no encontrado con ID: " + id);
    }
    updateName(updatedData, restaurant);
    updateAddress(updatedData, restaurant);
    updatePhoneNumber(updatedData, restaurant);
    updateOpeningHours(updatedData, restaurant);
    updateClosingHours(updatedData, restaurant);

    return repository.save(restaurant);
  }

  private static void updateClosingHours(Restaurant updatedData, Restaurant restaurant) {
    if (updatedData.getClosingHours() != null) restaurant.setClosingHours(updatedData.getClosingHours());
  }

  private static void updateOpeningHours(Restaurant updatedData, Restaurant restaurant) {
    if (updatedData.getOpeningHours() != null) restaurant.setOpeningHours(updatedData.getOpeningHours());
  }

  private static void updatePhoneNumber(Restaurant updatedData, Restaurant restaurant) {
    if (updatedData.getPhoneNumber() != null) restaurant.setPhoneNumber(updatedData.getPhoneNumber());
  }

  private static void updateAddress(Restaurant updatedData, Restaurant restaurant) {
    if (updatedData.getAddress() != null) restaurant.setAddress(updatedData.getAddress());
  }

  private static void updateName(Restaurant updatedData, Restaurant restaurant) {
    if (updatedData.getName() != null) restaurant.setName(updatedData.getName());
  }


  public void deleteRestaurant(Long id){
    if (!repository.existsById(id)) {
      throw new EmptyResultDataAccessException("Restaurante no encontrado con ID: " + id, 1);
    }
    repository.deleteById(id);
  }
}
