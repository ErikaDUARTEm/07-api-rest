package com.management.restaurant.DTO.restaurant;

import java.util.List;

public record DishPageResponseDTO(
  List<DishResponseDTO> content,
  int pageNumber,
  int pageSize,
  long totalElements,
  int totalPages
) {}
