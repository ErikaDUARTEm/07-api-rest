package com.management.restaurant.utils;

import com.management.restaurant.DTO.ClientResponseDTO;
import com.management.restaurant.models.client.Client;

public class DtoConverter {
  public static ClientResponseDTO convertToResponseDTO(Client cliente) {
    ClientResponseDTO dto = new ClientResponseDTO();
    dto.setId(cliente.getId());
    dto.setName(cliente.getName());
    dto.setEmail(cliente.getEmail());
    dto.setNumberPhone(cliente.getNumberPhone());
    dto.setIsFrecuent(cliente.getIsFrecuent());
    return dto;
  }
}
