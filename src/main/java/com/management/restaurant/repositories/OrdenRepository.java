package com.management.restaurant.repositories;

import com.management.restaurant.models.client.Client;
import com.management.restaurant.models.order.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

  @Query("SELECT COUNT(o) FROM Orden o WHERE o.client = :client")
  Integer countByClient(Client client);
}
