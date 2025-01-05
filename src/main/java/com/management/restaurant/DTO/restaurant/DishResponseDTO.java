package com.management.restaurant.DTO.restaurant;

import com.management.restaurant.models.restaurant.MenuRestaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishResponseDTO {
  private Long id;
  private String name;
  private Double price;
  private Boolean popular;
}
