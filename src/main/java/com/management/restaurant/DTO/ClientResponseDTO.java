package com.management.restaurant.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponseDTO {
  private Long id;
  @NotNull
  private String name;
  @NotNull
  private String email;
  @NotNull
  private String numberPhone;
  private Boolean isFrecuent;
}
