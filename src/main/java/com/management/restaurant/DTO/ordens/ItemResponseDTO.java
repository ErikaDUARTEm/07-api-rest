package com.management.restaurant.DTO.ordens;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemResponseDTO {
  private Long id;
  private String name;
  private Double price;
  private Integer quantity;
}
