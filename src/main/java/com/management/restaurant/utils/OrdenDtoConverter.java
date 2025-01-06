package com.management.restaurant.utils;

import com.management.restaurant.DTO.ordens.ItemRequestDTO;
import com.management.restaurant.DTO.ordens.OrdenRequestDTO;
import com.management.restaurant.DTO.ordens.OrdenResponseDTO;
import com.management.restaurant.models.client.Client;
import com.management.restaurant.models.order.Orden;

import java.util.Collections;

public class OrdenDtoConverter {

  public static OrdenResponseDTO convertToResponseDTO(Orden orden) {
    OrdenResponseDTO ordenResponseDTO = new OrdenResponseDTO();
    ordenResponseDTO.setId(orden.getOrdenId());
    ordenResponseDTO.setPriceTotal(orden.getPriceTotal());
    ordenResponseDTO.setDateOrder(orden.getDateOrder());
    ordenResponseDTO.setStatusOrder(orden.getStatusOrder());
    ordenResponseDTO.setClient(ClientDtoConverter.convertToResponseDTO(orden.getClient()));
    ordenResponseDTO.setItems(ItemDtoConverter.convertToResponseDTOList(orden.getItems()));
    return ordenResponseDTO;
  }

  public static Orden convertToEntity(OrdenRequestDTO ordenRequestDTO) {
    Orden orden = new Orden();
    orden.setPriceTotal(ordenRequestDTO.getPriceTotal());
    orden.setStatusOrder(ordenRequestDTO.getStatusOrder());

    Client client = new Client();
    client.setId(ordenRequestDTO.getClientId());
    orden.setClient(client);

    orden.setItems(ItemDtoConverter.convertToEntityList(Collections.singletonList((ItemRequestDTO) ordenRequestDTO.getItems())));
    return orden;
  }

}
