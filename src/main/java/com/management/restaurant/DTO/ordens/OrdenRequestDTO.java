package com.management.restaurant.DTO.ordens;

import com.management.restaurant.enums.StatusOrden;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrdenRequestDTO {
  @NotNull
  private Double priceTotal;

  @NotNull
  private StatusOrden statusOrder = StatusOrden.PENDING;

  @NotNull
  private Long clientId;

  private List<ItemRequestDTO> items;
}
