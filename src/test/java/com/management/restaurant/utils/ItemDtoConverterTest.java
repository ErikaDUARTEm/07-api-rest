package com.management.restaurant.utils;

import com.management.restaurant.DTO.ordens.ItemRequestDTO;
import com.management.restaurant.models.order.Item;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemDtoConverterTest {

  @Test
  void convertToEntityList() {
    ItemRequestDTO itemRequestDTO1 = new ItemRequestDTO();
    itemRequestDTO1.setName("Dish 1");
    itemRequestDTO1.setQuantity(2);
    itemRequestDTO1.setPrice(20.0);
    ItemRequestDTO itemRequestDTO2 = new ItemRequestDTO();
    itemRequestDTO2.setName("Dish 2");
    itemRequestDTO2.setQuantity(3);
    itemRequestDTO2.setPrice(15.0);
    List<ItemRequestDTO> itemRequestDTOs = Arrays.asList(itemRequestDTO1, itemRequestDTO2);
    List<Item> items = ItemDtoConverter.convertToEntityList(itemRequestDTOs);
    assertNotNull(items); assertEquals(2, items.size());
    assertEquals("Dish 1", items.get(0).getName());
    assertEquals(2, items.get(0).getQuantity());
    assertEquals(20.0, items.get(0).getPrice());
    assertEquals("Dish 2", items.get(1).getName());
    assertEquals(3, items.get(1).getQuantity());
    assertEquals(15.0, items.get(1).getPrice());
  }
}