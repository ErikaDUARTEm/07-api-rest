package com.management.restaurant.repositories;

import com.management.restaurant.models.restaurant.MenuRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<MenuRestaurant, Long> {
  Optional<MenuRestaurant> findByRestaurant_Id(Long restaurantId);
  @Query("SELECT m FROM MenuRestaurant m")
  List<MenuRestaurant> findAllMenus();

}
