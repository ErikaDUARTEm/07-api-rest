package com.management.restaurant.repositories;

import com.management.restaurant.models.order.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
  @Query(value = "SELECT SUM(quantity) FROM Item WHERE dish_id = :dishId", nativeQuery = true)
  Long countPopularDishes(@Param("dishId") Long dishId);

  void deleteByDishId(Long dishId);
}
