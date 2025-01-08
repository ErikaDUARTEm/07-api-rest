package com.management.restaurant.DTO.ordens;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemResponseDTO {
  private Long id;
  private String name;
  private Integer quantity;
  private DishDTO dish;
}
