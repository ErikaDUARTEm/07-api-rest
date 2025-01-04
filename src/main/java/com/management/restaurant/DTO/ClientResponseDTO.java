package com.management.restaurant.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponseDTO {
  private Long id;
  private String name;
  private String email;
  private String numberPhone;
  private Boolean isFrecuent;
}
