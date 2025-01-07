package com.management.restaurant.repositories;

import com.management.restaurant.models.restaurant.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
  @Query("SELECT d FROM Dish d")
  List<Dish> findAllDishes();

  @Query(value = "SELECT COUNT(*) FROM Item i WHERE i.dish_id = :dishId", nativeQuery = true)
  Long countPopularDishes(Long dishId);

  Dish findByName(String name);
}
