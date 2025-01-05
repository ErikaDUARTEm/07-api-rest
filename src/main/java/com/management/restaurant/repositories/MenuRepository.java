package com.management.restaurant.repositories;

import com.management.restaurant.models.restaurant.MenuRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<MenuRestaurant, Long> {
}
