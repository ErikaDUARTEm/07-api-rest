package com.management.restaurant.controllers;

import com.management.restaurant.DTO.client.ClientRequestDTO;
import com.management.restaurant.DTO.client.ClientResponseDTO;
import com.management.restaurant.models.client.Client;
import com.management.restaurant.services.ClientService;
import com.management.restaurant.utils.ClientDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

  private ClientService service;

  @Autowired
  public ClientController(ClientService service) {
    this.service = service;
  }

  @PostMapping()
  public ResponseEntity<String> addClient(@Validated @RequestBody ClientRequestDTO clientRequestDto) {
    Client client = new Client(
      clientRequestDto.getName(),
      clientRequestDto.getEmail(),
      clientRequestDto.getNumberPhone()
    );
    service.addClient(client);
    return ResponseEntity.ok("Cliente agregado exitosamente.");
  }
  @GetMapping
  public ResponseEntity<List<ClientResponseDTO>> listClient(){
    List<Client> clients = service.listClient();
    List<ClientResponseDTO> response = clients.stream()
      .map(ClientDtoConverter::convertToResponseDTO)
      .collect(Collectors.toList());
    return ResponseEntity.ok(response);
  }
  @GetMapping("/{id}")
  public ResponseEntity<ClientResponseDTO> showClientById(@PathVariable Long id){
   return service.showClientById(id)
     .map(client -> ResponseEntity.ok(ClientDtoConverter.convertToResponseDTO(client)))
     .orElse(ResponseEntity.notFound().build());
}
  @PutMapping("/{id}")
  public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable Long id, @RequestBody @Validated ClientRequestDTO clientRequestDTO) {
    try {
      Client client = ClientDtoConverter.convertToEntity(clientRequestDTO);
      Client actualizado = service.updateClient(id, client);
      return ResponseEntity.ok(ClientDtoConverter.convertToResponseDTO(actualizado));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ClientResponseDTO> deleteClient(@PathVariable Long id){
    try {
      service.deleteClient(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
}