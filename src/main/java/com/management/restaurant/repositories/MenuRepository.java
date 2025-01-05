package com.management.restaurant.repositories;

import com.management.restaurant.models.restaurant.MenuRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<MenuRestaurant, Long> {
  Optional<MenuRestaurant> findByRestaurantId(Long restaurantId);
}
