package com.management.restaurant.services;

import com.management.restaurant.models.client.Client;
import com.management.restaurant.repositories.ClientRepository;
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
@NoArgsConstructor
public class ClientService {

  private ClientRepository repository;

  @Autowired
  public ClientService(ClientRepository repository) {
    this.repository = repository;
  }
  public Client addClient(Client cliente){
    return repository.save(cliente);
  }

  public Optional<Client> showClientById(Long id){
    return repository.findById(id);
  }
  public List<Client> listClient(){
    return repository.findAll();
  }

  public Client updateClient(Long id, Client clientActualizado){
    return repository.findById(id).map(client -> {
     client.setName(clientActualizado.getName());
     client.setEmail(clientActualizado.getEmail());
     client.setNumberPhone(clientActualizado.getNumberPhone());
     client.setIsFrecuent(clientActualizado.getIsFrecuent());
     return repository.save(client);
    }).orElseThrow(()-> new RuntimeException("Cliente con el id "+id+" no pudo ser actualizado"));
  }

  public void deleteClient(Long id){
    repository.deleteById(id);
  }
}
