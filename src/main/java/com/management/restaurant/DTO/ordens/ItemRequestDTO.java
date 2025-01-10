package com.management.restaurant.DTO.ordens;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemRequestDTO {
  @NotNull
  private String name;

  @NotNull
  private Double price;

  @NotNull
  private Integer quantity;

  @NotNull
  private Long ordenId;

  public ItemRequestDTO(String name, Double price, Integer quantity) {
    this.name = name;
    this.price = price;
    this.quantity = quantity;

  }
}
