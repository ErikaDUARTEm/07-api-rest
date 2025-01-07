package com.management.restaurant.utils;

import com.management.restaurant.DTO.ordens.DishDTO;
import com.management.restaurant.DTO.ordens.ItemRequestDTO;
import com.management.restaurant.DTO.ordens.ItemResponseDTO;
import com.management.restaurant.models.order.Item;
import com.management.restaurant.models.restaurant.Dish;

import java.util.List;
import java.util.stream.Collectors;

public class ItemDtoConverter {

  public static ItemResponseDTO convertToResponseDTO(Item item) {
    ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
    itemResponseDTO.setId(item.getId());
    itemResponseDTO.setName(item.getName());
    itemResponseDTO.setQuantity(item.getQuantity());
    DishDTO dishDto = new DishDTO();
    dishDto.setId(item.getDish().getId());
    dishDto.setName(item.getDish().getName());
    dishDto.setPrice(item.getDish().getPrice());
    dishDto.setPopular(item.getDish().getPopular());
    itemResponseDTO.setDish(dishDto);
    return itemResponseDTO;
  }


  public static List<Item> convertToEntityList(List<ItemRequestDTO> itemRequestDTOs) {
    return itemRequestDTOs.stream()
      .map(ItemDtoConverter::convertToEntity)
      .collect(Collectors.toList());
  }

  public static Item convertToEntity(ItemRequestDTO itemRequestDTO) {
    Item item = new Item();
    item.setName(itemRequestDTO.getName());
    item.setPrice(itemRequestDTO.getPrice());
    item.setQuantity(itemRequestDTO.getQuantity());
    return item;
  }
  public static List<ItemResponseDTO> convertToResponseDTOList(List<Item> items) {
    return items.stream()
      .map(ItemDtoConverter::convertToResponseDTO)
      .collect(Collectors.toList());
  }

}
