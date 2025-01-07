package com.management.restaurant.factories;

import com.management.restaurant.enums.StatusOrden;
import com.management.restaurant.models.client.Client;
import com.management.restaurant.models.order.Item;
import com.management.restaurant.models.order.Orden;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrdenFactoryImplementation implements IOrdenFactory {
  @Override
  public Orden createOrden(Double priceTotal, LocalDateTime dateOrder, StatusOrden statusOrder, Client client,  List<Item> items) {
      Orden orden = new Orden();
      orden.setPriceTotal(priceTotal);
      orden.setDateOrder(dateOrder);
      orden.setStatusOrder(statusOrder);
      orden.setClient(client);
      orden.setItems(items);
      return orden;
  }
}
