package com.management.restaurant.DTO.restaurant;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class MenuResponseDTO {
  private Long id;
  private String description;
  private List<DishResponseDTO> dishes;
}
