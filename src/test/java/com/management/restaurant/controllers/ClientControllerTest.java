package com.management.restaurant.controllers;

import com.management.restaurant.DTO.client.ClientPageResponseDTO;
import com.management.restaurant.DTO.client.ClientRequestDTO;
import com.management.restaurant.DTO.client.ClientResponseDTO;
import com.management.restaurant.models.client.Client;
import com.management.restaurant.services.ClientService;
import com.management.restaurant.utils.ClientDtoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClientControllerTest {
  private final WebTestClient webTestClient;
  private final ClientService clientService;

  private Client existingClient;
  private Client updatedClient;
  private ClientRequestDTO clientRequestDTO;

  public ClientControllerTest(){
    clientService = mock(ClientService.class);
    webTestClient = WebTestClient.bindToController(new ClientController(clientService)).build();
  }
  @BeforeEach
  void setup() {
    existingClient = new Client(1L, "Aaron", "aaron@gmail.com", "386629292", false);
    updatedClient = new Client(1L, "Aaron Updated", "aaron.updated@gmail.com", "123456789", true);
    clientRequestDTO = new ClientRequestDTO();
    clientRequestDTO.setName("Aaron Updated");
    clientRequestDTO.setEmail("aaron.updated@gmail.com");
    clientRequestDTO.setNumberPhone("123456789");
    clientRequestDTO.setIsFrecuent(true);
  }
  @Test
  @DisplayName("Agregar un cliente")
  void addClient(){
    Client cliente = new Client( "luisa", "luisa@gmail.com", "3466667778");
    Client clienteConId = new Client( "luisa", "luisa@gmail.com", "3466667778");
    clienteConId.setId(1L);

    when(clientService.addClient(any(Client.class))).thenAnswer(invocation -> {
        Client c = invocation.getArgument(0);
        c.setId(1L);
        return c;
      });

      webTestClient
      .post()
      .uri("/api/cliente")
      .bodyValue(cliente)
      .exchange()
      .expectStatus().isCreated()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody(Client.class)
      .value(cliente1 -> {
        assertEquals(1L, cliente1.getId());
        assertEquals(cliente.getName(), cliente1.getName());
        assertEquals(cliente.getEmail(), cliente1.getEmail());
        assertEquals(cliente.getNumberPhone(), cliente1.getNumberPhone());

      });
    verify(clientService).addClient(any(Client.class));
  }
@Test
@DisplayName("Mostrar cliente por id")
void showClientById() {

  when(clientService.showClientById(existingClient.getId())).thenReturn(Optional.of(existingClient));

  webTestClient.get()
    .uri("/api/cliente/{id}", existingClient.getId())
    .exchange()
    .expectStatus().isOk()
    .expectHeader().contentType(MediaType.APPLICATION_JSON)
    .expectBody(ClientResponseDTO.class)
    .value(response -> {
      assertEquals(existingClient.getId(), response.getId());
      assertEquals(existingClient.getName(), response.getName());
      assertEquals(existingClient.getEmail(), response.getEmail());
      assertEquals(existingClient.getNumberPhone(), response.getNumberPhone());
      assertFalse(response.getIsFrecuent());
    });
  verify(clientService).showClientById(existingClient.getId());
}
  @Test
  @DisplayName("Lista de clientes paginada")
  void listClients() {
    List<Client> clients = List.of(
      new Client(1L, "Aaron", "aaron@gmail.com", "386629292", false),
      new Client(2L, "Lila", "lila@gmail.com", "987654321", false),
      new Client(3L, "Pedro", "pedro@gmail.com", "555666777", true)
    );

    List<ClientResponseDTO> clientDTOs = clients.stream()
      .map(ClientDtoConverter::convertToResponseDTO)
      .toList();

    Page<ClientResponseDTO> page = new PageImpl<>(clientDTOs, PageRequest.of(0, 5), clientDTOs.size());

    when(clientService.listClient(any(Pageable.class))).thenReturn(page);

    webTestClient.get()
      .uri("/api/cliente?page=0&size=5")
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody(ClientPageResponseDTO.class)
      .value(response -> {
        assertEquals(3, response.content().size());
        assertEquals(0, response.pageNumber());
        assertEquals(5, response.pageSize());
        assertEquals(3, response.totalElements());
        assertEquals(1, response.totalPages());

        assertEquals("Aaron", response.content().get(0).getName());
        assertEquals("Lila", response.content().get(1).getName());
        assertEquals("Pedro", response.content().get(2).getName());
      });

    verify(clientService).listClient(any(Pageable.class));
  }

  @Test
  @DisplayName("Actualizar cliente por id")
  void updateClient(){
    when(clientService.updateClient(eq(existingClient.getId()), any(Client.class))).thenReturn(updatedClient);
    webTestClient.put()
      .uri("/api/cliente/{id}", existingClient.getId())
      .bodyValue(clientRequestDTO)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody(ClientResponseDTO.class)
      .value(response -> {
        assertEquals(updatedClient.getId(), response.getId());
        assertEquals(updatedClient.getName(), response.getName());
        assertEquals(updatedClient.getEmail(), response.getEmail());
        assertEquals(updatedClient.getNumberPhone(), response.getNumberPhone());
        assertTrue(updatedClient.getIsFrecuent());
      });
    verify(clientService).updateClient(eq(existingClient.getId()), any(Client.class));
  }

  @Test
  @DisplayName("Eliminar cliente")
  void deleteClient(){

    when(clientService.showClientById(existingClient.getId())).thenReturn(Optional.of(existingClient));
    doNothing().when(clientService).deleteClient(existingClient.getId());

    webTestClient.delete()
      .uri("/api/cliente/{id}", existingClient.getId())
      .exchange()
      .expectStatus().isNoContent();

    verify(clientService).deleteClient(existingClient.getId());
  }
}
