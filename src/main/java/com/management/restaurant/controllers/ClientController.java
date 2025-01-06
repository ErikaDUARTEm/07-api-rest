package com.management.restaurant.controllers;

import com.management.restaurant.DTO.client.ClientRequestDTO;
import com.management.restaurant.DTO.client.ClientResponseDTO;
import com.management.restaurant.models.client.Client;
import com.management.restaurant.services.ClientService;
import com.management.restaurant.utils.ClientDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cliente")
public class ClientController {

  private final ClientService service;

  @Autowired
  public ClientController(ClientService service) {
    this.service = service;
  }

  @PostMapping()
  public ClientResponseDTO addClient(@Validated @RequestBody ClientRequestDTO clientRequestDto) {
    Client client = new Client(
      clientRequestDto.getName(),
      clientRequestDto.getEmail(),
      clientRequestDto.getNumberPhone()
    );
    Client addedClient = service.addClient(client);
    return ClientDtoConverter.convertToResponseDTO(addedClient);
  }

  @GetMapping
  public List<ClientResponseDTO> listClient() {
    List<Client> clients = service.listClient();
    return clients.stream()
      .map(ClientDtoConverter::convertToResponseDTO)
      .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public ClientResponseDTO showClientById(@PathVariable Long id) {
    return service.showClientById(id)
      .map(ClientDtoConverter::convertToResponseDTO)
      .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
  }

  @PutMapping("/{id}")
  public ClientResponseDTO updateClient(@PathVariable Long id, @RequestBody @Validated ClientRequestDTO clientRequestDTO) {
    Client client = ClientDtoConverter.convertToEntity(clientRequestDTO);
    Client updatedClient = service.updateClient(id, client);
    return ClientDtoConverter.convertToResponseDTO(updatedClient);
  }

  @DeleteMapping("/{id}")
  public void deleteClient(@PathVariable Long id) {
    service.deleteClient(id);
  }
}
