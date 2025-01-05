package com.management.restaurant.repositories;

import com.management.restaurant.models.restaurant.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {
}
