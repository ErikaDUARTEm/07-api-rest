package com.management.restaurant.repositories;

import com.management.restaurant.models.order.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

  @Query(value = "SELECT COUNT(*) FROM orden WHERE client_id = :clientId", nativeQuery = true)
  Integer countByClientId(@Param("clientId") Long clientId);

  @Query("SELECT o FROM Orden o WHERE o.client.id = :clientId AND o.dateOrder = :dateOrder")
  List<Orden> findByClientAndDateOrder(@Param("clientId") Long clientId, @Param("dateOrder") LocalDateTime dateOrder);
}
