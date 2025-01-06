package com.management.restaurant.services;

import com.management.restaurant.DTO.ordens.OrdenRequestDTO;
import com.management.restaurant.DTO.ordens.OrdenResponseDTO;
import com.management.restaurant.enums.StatusOrden;
import com.management.restaurant.factories.OrdenFactory;
import com.management.restaurant.models.client.Client;
import com.management.restaurant.models.order.Item;
import com.management.restaurant.models.order.Orden;
import com.management.restaurant.repositories.ClientRepository;
import com.management.restaurant.repositories.OrdenRepository;
import com.management.restaurant.utils.ItemDtoConverter;
import com.management.restaurant.utils.OrdenDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenService {
  private final OrdenRepository ordenRepository;
  private final OrdenFactory ordenFactory;
  private final ClientRepository clientRepository;

  @Autowired
  public OrdenService(OrdenRepository ordenRepository, OrdenFactory ordenFactory, ClientRepository clientRepository) {
    this.ordenRepository = ordenRepository;
    this.ordenFactory = ordenFactory;
    this.clientRepository = clientRepository;
  }

  public OrdenResponseDTO createOrden(OrdenRequestDTO ordenRequestDTO) {
    LocalDateTime dateOrder = LocalDateTime.now();
    StatusOrden statusOrder = StatusOrden.PENDING;

    Client client = clientRepository.findById(ordenRequestDTO.getClientId())
      .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

    if (ordenRequestDTO.getItems() == null || ordenRequestDTO.getItems().isEmpty()) {
      throw new IllegalArgumentException("El pedido debe tener al menos un item.");
    }

    List<Item> items = ItemDtoConverter.convertToEntityList(ordenRequestDTO.getItems());
    Orden orden = ordenFactory.createOrden(ordenRequestDTO.getPriceTotal(), dateOrder, statusOrder, client, items);

    for (Item item : items) {
      item.setOrden(orden);
    }

    orden = ordenRepository.save(orden);
    return OrdenDtoConverter.convertToResponseDTO(orden);
  }


  public List<OrdenResponseDTO> getAllOrdenes() {
    return ordenRepository.findAll().stream()
      .map(OrdenDtoConverter::convertToResponseDTO)
      .collect(Collectors.toList());
  }

  public OrdenResponseDTO getOrdenById(Long id) {
    Orden orden = ordenRepository.findById(id).orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    return OrdenDtoConverter.convertToResponseDTO(orden);
  }

  public OrdenResponseDTO updateOrden(Long id, OrdenRequestDTO ordenRequestDTO) {
    Orden orden = ordenRepository.findById(id).orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    orden.setPriceTotal(ordenRequestDTO.getPriceTotal());
    orden.setStatusOrder(ordenRequestDTO.getStatusOrder());
    orden.setClient(new Client(ordenRequestDTO.getClientId()));
    return OrdenDtoConverter.convertToResponseDTO(ordenRepository.save(orden));
  }

  public void deleteOrden(Long id) {
    ordenRepository.deleteById(id);
  }
}
