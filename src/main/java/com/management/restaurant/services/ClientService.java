package com.management.restaurant.services;

import com.management.restaurant.models.client.Client;
import com.management.restaurant.repositories.ClientRepository;
import com.management.restaurant.repositories.OrdenRepository;
import com.management.restaurant.services.interfaces.IObserver;
import com.management.restaurant.services.observer.ObserverManager;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
@NoArgsConstructor(force = true)
public class ClientService implements IObserver<Client> {
  private final ObserverManager<Client> observerManager;
  private final ClientRepository clientRepository;
  private final OrdenRepository ordenRepository;

  @Autowired
  public ClientService(ObserverManager<Client> observerManager, ClientRepository clientRepository, OrdenRepository ordenRepository) {
    this.observerManager = observerManager;
    this.clientRepository = clientRepository;
    this.ordenRepository = ordenRepository;
    addClientObserver();
  }
  public Client addClient(Client cliente){
    assert clientRepository != null;
    Client savedClient = clientRepository.save(cliente);
    notifyClientObservers(savedClient);
    return savedClient;
  }

  public Optional<Client> showClientById(Long id){
    return clientRepository.findById(id);
  }

  public List<Client> listClient(){
    assert clientRepository != null;
    return clientRepository.findAll();
  }

  public Client updateClient(Long id, Client clientUpdated) {
    assert clientRepository != null;
    return clientRepository.findById(id).map(client -> {
      updateClientDetails(client, clientUpdated);
      return clientRepository.save(client);
    }).orElseThrow(() -> new RuntimeException("Cliente con el id " + id + " no pudo ser actualizado"));
  }
  private void updateClientName(Client client, Client clientUpdated) {
    if (clientUpdated.getName() != null) {
      client.setName(clientUpdated.getName());
    }
  }

  private void updateClientEmail(Client client, Client clientUpdated) {
    if (clientUpdated.getEmail() != null) {
      client.setEmail(clientUpdated.getEmail());
    }
  }

  private void updateClientNumberPhone(Client client, Client clientUpdated) {
    if (clientUpdated.getNumberPhone() != null) {
      client.setNumberPhone(clientUpdated.getNumberPhone());
    }
  }

  private void updateClientIsFrecuent(Client client, Client clientUpdated) {
    if (clientUpdated.getIsFrecuent() != null) {
      client.setIsFrecuent(clientUpdated.getIsFrecuent());
    }
  }

  private void updateClientDetails(Client client, Client clientUpdated) {
    updateClientName(client, clientUpdated);
    updateClientEmail(client, clientUpdated);
    updateClientNumberPhone(client, clientUpdated);
    updateClientIsFrecuent(client, clientUpdated);
  }

  public void deleteClient(Long id) {
    assert clientRepository != null;
    Client client = clientRepository.findById(id).orElseThrow(() ->
      new IllegalArgumentException("Cliente no encontrado"));

    assert observerManager != null;
    removeClientObserver(this);
    clientRepository.deleteById(id);
    }

  public void addClientObserver() {
    assert observerManager != null;
    observerManager.addObserver(this);
  }

  public void removeClientObserver(IObserver<Client> IObserver) {
    assert observerManager != null;
    observerManager.removeObserver(IObserver);
  }

  public void notifyClientObservers(Client client) {
    if (client == null) {
      throw new IllegalArgumentException("El cliente no puede ser nulo al notificar a los observadores.");
    }
    assert observerManager != null;
    observerManager.notifyObservers(client);
  }

  @Override
  public void updateObserver(Client client) {
    assert ordenRepository != null;
    int numeroPedidos = ordenRepository.countByClientId(client.getId());
    if (numeroPedidos >= 10 && !client.getIsFrecuent()) {
      client.setIsFrecuent(true);
      assert clientRepository != null;
      clientRepository.save(client);
    }
  }
}
