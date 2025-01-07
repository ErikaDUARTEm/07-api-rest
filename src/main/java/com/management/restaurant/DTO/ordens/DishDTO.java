package com.management.restaurant.DTO.ordens;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DishDTO {
  private Long id;
  private String name;
  private Double price;
  private Boolean popular;
}
