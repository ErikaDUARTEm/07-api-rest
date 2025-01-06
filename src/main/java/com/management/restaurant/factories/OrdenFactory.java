package com.management.restaurant.factories;

import com.management.restaurant.enums.StatusOrden;
import com.management.restaurant.models.client.Client;
import com.management.restaurant.models.order.Item;
import com.management.restaurant.models.order.Orden;


import java.time.LocalDateTime;
import java.util.List;

public interface OrdenFactory {
    Orden createOrden(Double priceTotal, LocalDateTime dateOrder, StatusOrden statusOrder, Client client, List<Item> items);
}
