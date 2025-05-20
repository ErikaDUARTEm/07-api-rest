package com.management.restaurant.DTO.ordens;

import java.util.List;

public record OrdenPageResponseDTO(
  List<OrdenResponseDTO> content,
  int pageNumber,
  int pageSize,
  long totalElements,
  int totalPages) {
}
