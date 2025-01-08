package com.management.restaurant.services;

import com.management.restaurant.models.client.Client;
import com.management.restaurant.repositories.ClientRepository;
import com.management.restaurant.repositories.OrdenRepository;
import com.management.restaurant.services.interfaces.IObserver;
import com.management.restaurant.services.observer.ObserverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClientServiceTest {
  private ClientRepository clientRepository;
  private OrdenRepository ordenRepository;
  private ObserverManager<Client> observerManager;
  private ClientService clientService;

  private Client existingClient;
  private Client updatedClient;


  @BeforeEach
  void setUp() {
    clientRepository = mock(ClientRepository.class);
    ordenRepository = mock(OrdenRepository.class);
    observerManager = mock(ObserverManager.class);

    clientService = new ClientService(observerManager, clientRepository, ordenRepository);
    existingClient = new Client(1L, "Aaron", "aaron@gmail.com", "386629292", false);
    updatedClient = new Client(1L, "Aaron Updated", "aaron.updated@gmail.com", "123456789", true);
  }

  @Test
  @DisplayName("Agregar un cliente")
  void addClient() {
    Client client = new Client("Aaron", "aaron@gmail.com", "386629292", false);
    when(clientRepository.save(any(Client.class))).thenReturn(client);

    Client savedClient = clientService.addClient(client);
    assertEquals("Aaron", savedClient.getName());
    assertEquals("aaron@gmail.com", savedClient.getEmail());
    verify(clientRepository).save(client);
    verify(observerManager).notifyObservers(client);
  }

  @Test
  @DisplayName("Mostrar cliente por id")
  void showClientById() {
    when(clientRepository.findById(existingClient.getId())).thenReturn(Optional.of(existingClient));
    Optional<Client> foundClient = clientService.showClientById(existingClient.getId());
    assertEquals(Optional.of(existingClient), foundClient);
    verify(clientRepository).findById(existingClient.getId());
  }

  @Test
  @DisplayName("Listar clientes")
  void listClient() {
    List<Client> clients = List.of(
      new Client(1L, "Aaron", "aaron@gmail.com", "386629292", false),
      new Client(2L, "Lila", "lila@gmail.com", "987654321", false),
      new Client(3L, "Pedro", "pedro@gmail.com", "555666777", true)
    );
    when(clientRepository.findAll()).thenReturn(clients);
    List<Client> allClients = clientService.listClient();
    assertEquals(3, allClients.size());
    verify(clientRepository).findAll();
  }

  @Test
  @DisplayName("Actualizar cliente por id")
  void updateClient() {
    when(clientRepository.findById(existingClient.getId())).thenReturn(Optional.of(existingClient));
    when(clientRepository.save(any(Client.class))).thenReturn(updatedClient);

    Client result = clientService.updateClient(existingClient.getId(), updatedClient);
    assertEquals(updatedClient.getName(), result.getName());
    assertEquals(updatedClient.getEmail(), result.getEmail());
    assertEquals(updatedClient.getNumberPhone(), result.getNumberPhone());
    assertEquals(updatedClient.getIsFrecuent(), result.getIsFrecuent());

    verify(clientRepository).findById(existingClient.getId());
    verify(clientRepository).save(existingClient);
  }

  @Test
  @DisplayName("Eliminar cliente")
  void deleteClient() {
    when(clientRepository.findById(existingClient.getId())).thenReturn(Optional.of(existingClient));
    doNothing().when(clientRepository).deleteById(existingClient.getId());
    clientService.deleteClient(existingClient.getId());
    verify(clientRepository).findById(existingClient.getId());
    verify(clientRepository).deleteById(existingClient.getId());
  }
  @Test
  @DisplayName("Eliminar cliente- cuando devuelve exception cliente no encontrado")
  void deleteClientNoFound() {
     Long nonExistentClientId = 99L;
      when(clientRepository.findById(nonExistentClientId)).thenReturn(Optional.empty());

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      clientService.deleteClient(nonExistentClientId);
    });
    assertEquals("Cliente no encontrado",
      exception.getMessage());
    verify(clientRepository, times(1)).findById(nonExistentClientId);
    verify(observerManager, never()).removeObserver(clientService);
    verify(clientRepository, never()).deleteById(nonExistentClientId);
  }

  @Test
  @DisplayName("Agregar un observer")
  void addClientObserver() {
    clientService.addClientObserver();
    verify(observerManager, times(2)).addObserver(clientService);
  }

  @Test
  @DisplayName("Remover un observer")
  void removeClientObserver() {
    IObserver<Client> observer = mock(IObserver.class);
    clientService.removeClientObserver(observer);
    verify(observerManager, times(1)).removeObserver(observer);
  }

  @Test
  @DisplayName("Notificar al observer con cliente null")
  void notifyClientObservers() {
    assertThrows(IllegalArgumentException.class, () -> {
      clientService.notifyClientObservers(null);
    });
  }

  @Test
  @DisplayName("Notificar al observer")
  void notifyClientObservers_withValidClient() {
    Client client = mock(Client.class);
    clientService.notifyClientObservers(client);
    verify(observerManager, times(1)).notifyObservers(client);
  }

  @Test
  @DisplayName("Actualizar cliente cuando es frecuente")
  void updateObserver() {
    when(ordenRepository.countByClientId(anyLong())).thenReturn(11);
    clientService.updateObserver(existingClient);
    assertTrue(existingClient.getIsFrecuent());
    verify(clientRepository, times(1)).save(existingClient);
  }

  @Test
  @DisplayName("Actualizar observer con cliente no frecuente")
  void updateObserver_withNonFrequentClient() {
    when(ordenRepository.countByClientId(anyLong())).thenReturn(5);
    clientService.updateObserver(existingClient);
    assertFalse(existingClient.getIsFrecuent());
    verify(clientRepository, never()).save(existingClient);
  }

  @Test
  @DisplayName("Probar getter de ObserverManager")
  void getObserverManager() {
    ObserverManager<Client> observerManager = clientService.getObserverManager();
    assertNotNull(observerManager);
  }

  @Test
  @DisplayName("Probar getter de ClientRepository")
  void getClientRepository() {
    ClientRepository clientRepository = clientService.getClientRepository();
    assertNotNull(clientRepository);
  }

  @Test
  @DisplayName("Probar getter de OrdenRepository")
  void getOrdenRepository() {
    OrdenRepository ordenRepository = clientService.getOrdenRepository();
    assertNotNull(ordenRepository);
  }
}