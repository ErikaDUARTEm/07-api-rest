package com.management.restaurant.strategy;

import com.management.restaurant.models.order.Orden;

public interface IStatusOrdenStrategy {
  void handle(Orden orden);
}
