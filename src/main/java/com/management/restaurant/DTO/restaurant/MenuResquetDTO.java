package com.management.restaurant.DTO.restaurant;

import com.management.restaurant.models.restaurant.Dish;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuResquetDTO {
  @NotNull
  private String description;
  private List<DishRequestDTO> dishes;
  @NotNull
  private Long restaurantId;
}
