package com.management.restaurant.strategy;

import com.management.restaurant.enums.StatusOrden;
import com.management.restaurant.models.order.Orden;
import org.springframework.stereotype.Service;

@Service
public class StatusCancelled implements IStatusOrdenStrategy{
  @Override
  public void handle(Orden orden) {
    orden.setStatusOrder(StatusOrden.CANCELLED);
  }
}
