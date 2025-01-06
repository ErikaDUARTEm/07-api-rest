package com.management.restaurant.repositories;

import com.management.restaurant.models.order.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
}
