package com.management.restaurant.repositories;

import com.management.restaurant.models.restaurant.MenuRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<MenuRestaurant, Long> {
  Optional<MenuRestaurant> findByRestaurant_Id(Long restaurantId);
  Optional<MenuRestaurant> findByIdMenuAndRestaurant_Id(Long idMenu, Long restaurantId);
}
