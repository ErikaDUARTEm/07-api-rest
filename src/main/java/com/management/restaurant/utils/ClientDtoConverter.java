package com.management.restaurant.utils;

import com.management.restaurant.DTO.client.ClientRequestDTO;
import com.management.restaurant.DTO.client.ClientResponseDTO;
import com.management.restaurant.models.client.Client;


public class ClientDtoConverter {

  public static ClientResponseDTO convertToResponseDTO(Client cliente) {
    ClientResponseDTO dto = new ClientResponseDTO();
    dto.setId(cliente.getId());
    dto.setName(cliente.getName());
    dto.setEmail(cliente.getEmail());
    dto.setNumberPhone(cliente.getNumberPhone());
    dto.setIsFrecuent(cliente.getIsFrecuent());
    return dto;
  }
  public static Client convertToEntity(ClientRequestDTO clientRequestDTO) {
    Client client = new Client();
    client.setName(clientRequestDTO.getName());
    client.setEmail(clientRequestDTO.getEmail());
    client.setNumberPhone(clientRequestDTO.getNumberPhone());
    client.setIsFrecuent(clientRequestDTO.getIsFrecuent());
    return client;
  }


}
