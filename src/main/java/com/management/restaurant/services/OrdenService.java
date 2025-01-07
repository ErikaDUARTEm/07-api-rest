package com.management.restaurant.services;

import com.management.restaurant.DTO.ordens.ItemRequestDTO;
import com.management.restaurant.DTO.ordens.OrdenRequestDTO;
import com.management.restaurant.DTO.ordens.OrdenResponseDTO;
import com.management.restaurant.enums.StatusOrden;
import com.management.restaurant.factories.OrdenFactory;
import com.management.restaurant.models.client.Client;
import com.management.restaurant.models.order.Item;
import com.management.restaurant.models.order.Orden;
import com.management.restaurant.models.restaurant.Dish;
import com.management.restaurant.repositories.ClientRepository;
import com.management.restaurant.repositories.DishRepository;
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
  private final ClientService clientService;
  private final DishService dishService;

  @Autowired
  public OrdenService(OrdenRepository ordenRepository, OrdenFactory ordenFactory, ClientRepository clientRepository, DishRepository dishRepository, ClientService clientService, DishService dishService) {
    this.ordenRepository = ordenRepository;
    this.ordenFactory = ordenFactory;
    this.clientRepository = clientRepository;
    this.clientService = clientService;
    this.dishService = dishService;

  }

  public OrdenResponseDTO createOrden(OrdenRequestDTO ordenRequestDTO) {
    LocalDateTime dateOrder = LocalDateTime.now();
    StatusOrden statusOrder = StatusOrden.PENDING;
    Client client = findClientById(ordenRequestDTO.getClientId());
    List<Item> items = validateAndConvertItems(ordenRequestDTO.getItems());

    double priceTotal = calculateTotalPrice(items);
    clientService.updateObserver(client);
    if (client.getIsFrecuent()) {
      priceTotal = applyDiscount(priceTotal, 2.38);
    }
    Orden orden = createAndSaveOrden(ordenRequestDTO, dateOrder, statusOrder, client, items,priceTotal);
    clientService.notifyClientObservers(orden.getClient());
    items.forEach(item ->{
      Dish dish = item.getDish();
      if(dish != null){
        notifyDishObserversForItems(items);
        dishService.updateObserver(dish);
        item.setDish(dishService.findDishByName(item.getName()));
        if(dish.getPopular()){
          item.setPrice(dish.getPrice());
        }
      }
    });
    priceTotal = calculateTotalPrice(items);
    orden.setPriceTotal(priceTotal);
    return OrdenDtoConverter.convertToResponseDTO(orden);
  }

  private Client findClientById(Long clientId) {
    return clientRepository.findById(clientId)
      .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
  }

  private List<Item> validateAndConvertItems(List<ItemRequestDTO> items) {
      if (items == null || items.isEmpty()) {
        throw new IllegalArgumentException("El pedido debe tener al menos un item.");
      }
      return items.stream()
        .map(this::convertAndValidateItem)
        .collect(Collectors.toList());
  }
  private Item convertAndValidateItem(ItemRequestDTO itemDTO) {
    Dish dish = findDishByName(itemDTO.getName());
    Item item = ItemDtoConverter.convertToEntity(itemDTO);
    item.setDish(dish);
    return item;
  }
  private Dish findDishByName(String name) {
    Dish dish = dishService.findDishByName(name);
    if (dish == null) {
      throw new RuntimeException("El plato con nombre " + name + " no existe");
    }
    return dish;
  }

  private Orden createAndSaveOrden(OrdenRequestDTO ordenRequestDTO, LocalDateTime dateOrder, StatusOrden statusOrder, Client client, List<Item> items, Double priceTotal) {
    Orden orden = ordenFactory.createOrden(ordenRequestDTO.getPriceTotal(), dateOrder, statusOrder, client, items);
    orden.setPriceTotal(priceTotal);
    items.forEach(item -> setItemOrdenAndDish(item, orden));
    return ordenRepository.save(orden);
  }
  private void setItemOrdenAndDish(Item item, Orden orden) {
    item.setOrden(orden);
    item.setDish(findDishByName(item.getName()));
  }
  private Double calculateTotalPrice(List<Item> items) {
    return items.stream()
      .mapToDouble(item -> item.getPrice() * item.getQuantity())
      .sum();
  }

  private Double applyDiscount(Double priceTotal, Double discountPercentage) {
    return priceTotal * ((100 - discountPercentage) / 100);
  }

  public List<OrdenResponseDTO> getAllOrdenes() {
    try {
      return ordenRepository.findAll().stream()
        .map(OrdenDtoConverter::convertToResponseDTO)
        .collect(Collectors.toList());
    } catch (Exception e) {
      throw new RuntimeException("Error al obtener todas las órdenes", e);
    }
  }

  public OrdenResponseDTO getOrdenById(Long id) {
    Orden orden = ordenRepository.findById(id).orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    return OrdenDtoConverter.convertToResponseDTO(orden);
  }

  public OrdenResponseDTO updateOrden(Long id, OrdenRequestDTO ordenRequestDTO) {
    Orden orden = ordenRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

    Client client = clientRepository.findById(ordenRequestDTO.getClientId())
      .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    List<Item> items = validateAndConvertItems(ordenRequestDTO.getItems());
    orden.setItems(items);
    orden.setPriceTotal(calculateTotalPrice(items));
    orden.setStatusOrder(ordenRequestDTO.getStatusOrder());
    orden.setClient(client);
    Orden updatedOrden = ordenRepository.save(orden);
    return OrdenDtoConverter.convertToResponseDTO(updatedOrden);
  }

  public void deleteOrden(Long id) {
    ordenRepository.deleteById(id);
  }
  private void notifyDishObserversForItems(List<Item> items) {
    items.forEach(item -> {
      Dish dish = item.getDish();
      if (dish != null) {
        dishService.notifyDishObservers(dish);
      }
    });
  }
}
