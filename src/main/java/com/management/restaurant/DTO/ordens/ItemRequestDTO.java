package com.management.restaurant.DTO.ordens;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequestDTO {
  @NotNull
  private String name;

  @NotNull
  private Double price;

  @NotNull
  private Integer quantity;

  @NotNull
  private Long ordenId;
}
