package com.management.restaurant.repositories;

import com.management.restaurant.models.restaurant.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
  @Query("SELECT d FROM Dish d")
  List<Dish> findAllDishes();
}
