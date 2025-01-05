package com.management.restaurant.DTO.restaurant;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishRequestDTO {
  @NotNull
  private String name;
  @NotNull private Double price;
  private Boolean popular;
}
