package com.management.restaurant.repositories;

import com.management.restaurant.models.restaurant.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
  @Query("SELECT d FROM Dish d")
  List<Dish> findAllDishes();

  Dish findByName(String name);

  @Query(value = "SELECT d.* FROM Dish d " + "JOIN menu_restaurant mr ON d.menu_restaurant_id = mr.id_menu " + "JOIN Restaurant r ON mr.restaurant_id = r.id " + "WHERE d.name = :name " + "AND r.id = :restaurantId " + "AND mr.id_menu = :menuId", nativeQuery = true)
  Dish findByNameAndMenuRestaurantRestaurantIdAndMenuRestaurantMenuId(@Param("name") String name, @Param("restaurantId") Long restaurantId, @Param("menuId") Long menuId);
}
