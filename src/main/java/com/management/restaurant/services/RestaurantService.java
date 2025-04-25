package com.management.restaurant.services;

import com.management.restaurant.models.restaurant.Restaurant;
import com.management.restaurant.repositories.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
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
  public Restaurant updateRestaurant(Long id, Restaurant updateRestaurant){
    Restaurant restaurantById = repository.findRestaurantById(id);

    if (restaurantById == null) {
      throw new EntityNotFoundException("Restaurante no encontrado con ID: " + id);
    }
    updateName(updateRestaurant, restaurantById);
    updateAddress(updateRestaurant, restaurantById);
    updatePhoneNumber(updateRestaurant, restaurantById);
    updateOpeningHours(updateRestaurant, restaurantById);
    updateClosingHours(updateRestaurant, restaurantById);

    return repository.save(restaurantById);
  }

  private static void updateClosingHours(Restaurant updateRestaurant, Restaurant restaurantById) {
    if (updateRestaurant.getClosingHours() != null) restaurantById.setClosingHours(updateRestaurant.getClosingHours());
  }

  private static void updateOpeningHours(Restaurant updateRestaurant, Restaurant restaurantById) {
    if (updateRestaurant.getOpeningHours() != null) restaurantById.setOpeningHours(updateRestaurant.getOpeningHours());
  }

  private static void updatePhoneNumber(Restaurant updateRestaurant, Restaurant restaurantById) {
    if (updateRestaurant.getPhoneNumber() != null) restaurantById.setPhoneNumber(updateRestaurant.getPhoneNumber());
  }

  private static void updateAddress(Restaurant updateRestaurant, Restaurant restaurantById) {
    if (updateRestaurant.getAddress() != null) restaurantById.setAddress(updateRestaurant.getAddress());
  }

  private static void updateName(Restaurant updateRestaurant, Restaurant restaurantById) {
    if (updateRestaurant.getName() != null) restaurantById.setName(updateRestaurant.getName());
  }

  public void deleteRestaurant(Long id){
    if (!repository.existsById(id)) {
      throw new EmptyResultDataAccessException("Restaurante no encontrado con ID: " + id, 1);
    }
    repository.deleteById(id);
  }

}
