package com.management.restaurant.repositories;

import com.management.restaurant.models.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
  @Query("SELECT r FROM Restaurant r JOIN FETCH r.menuRestaurant WHERE r.id = :restaurantId")
  Restaurant findMenuByRestaurant(Long restaurantId);

}
