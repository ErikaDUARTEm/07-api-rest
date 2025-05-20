package com.management.restaurant.DTO.client;

import java.util.List;

public record ClientPageResponseDTO(
  List<ClientResponseDTO> content,
  int pageNumber,
  int pageSize,
  long totalElements,
  int totalPages) {
}
