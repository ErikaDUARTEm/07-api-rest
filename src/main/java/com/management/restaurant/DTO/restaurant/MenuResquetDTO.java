package com.management.restaurant.DTO.restaurant;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuResquetDTO {
  @NotNull
  private String description;
  @NotNull
  private Long restaurantId;
  @NotNull
  private List<DishRequestDTO> dishes;
}
