package com.management.restaurant.services;

import com.management.restaurant.DTO.ordens.ItemRequestDTO;
import com.management.restaurant.DTO.ordens.OrdenRequestDTO;
import com.management.restaurant.DTO.ordens.OrdenResponseDTO;
import com.management.restaurant.enums.StatusOrden;
import com.management.restaurant.factories.IOrdenFactory;
import com.management.restaurant.models.client.Client;
import com.management.restaurant.models.order.Item;
import com.management.restaurant.models.order.Orden;
import com.management.restaurant.models.restaurant.Dish;
import com.management.restaurant.repositories.ClientRepository;
import com.management.restaurant.repositories.OrdenRepository;
import com.management.restaurant.strategy.IStatusOrdenStrategy;
import com.management.restaurant.strategy.StateInPreparation;
import com.management.restaurant.strategy.StatusCancelled;
import com.management.restaurant.strategy.StatusCompleted;
import com.management.restaurant.strategy.StatusDelivered;
import com.management.restaurant.utils.ItemDtoConverter;
import com.management.restaurant.utils.OrdenDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrdenService {
  private final OrdenRepository ordenRepository;
  private final IOrdenFactory IOrdenFactory;
  private final ClientRepository clientRepository;
  private final ClientService clientService;
  private final DishService dishService;
  private final Map<StatusOrden, IStatusOrdenStrategy> statusStrategy;

  @Autowired
  public OrdenService(OrdenRepository ordenRepository, IOrdenFactory IOrdenFactory, ClientRepository clientRepository, ClientService clientService, DishService dishService) {
    this.ordenRepository = ordenRepository;
    this.IOrdenFactory = IOrdenFactory;
    this.clientRepository = clientRepository;
    this.clientService = clientService;
    this.dishService = dishService;

    statusStrategy = new HashMap<>();
    statusStrategy.put(StatusOrden.IN_PREPARATION, new StateInPreparation());
    statusStrategy.put(StatusOrden.COMPLETED, new StatusCompleted());
    statusStrategy.put(StatusOrden.CANCELLED, new StatusCancelled());
    statusStrategy.put(StatusOrden.DELIVERED, new StatusDelivered());

  }

  public OrdenResponseDTO createOrden(OrdenRequestDTO ordenRequestDTO) {
    try {
      LocalDateTime dateOrder = LocalDateTime.now();
      StatusOrden statusOrder = StatusOrden.PENDING;
      Client client = findClientById(ordenRequestDTO.getClientId());
      List<Item> items = validateAndConvertItems(ordenRequestDTO.getItems());

      double priceTotal = calculateTotalPrice(items);
      clientService.updateObserver(client);

      Orden orden = createAndSaveOrden(ordenRequestDTO, dateOrder, statusOrder, client, items, priceTotal);
      clientService.notifyClientObservers(orden.getClient());

      adjustItemPrices(items);

      priceTotal = calculateTotalPrice(items);
      if (client.getIsFrecuent()) {
        priceTotal = applyDiscount(priceTotal, 2.38);
      }
      orden.setItems(items);
      orden.setPriceTotal(priceTotal);
      ordenRepository.save(orden);
      return OrdenDtoConverter.convertToResponseDTO(orden);
    }catch (Exception e) { e.printStackTrace();
      throw new RuntimeException("Error al crear la orden: " + e.getMessage());
    }
  }
  public Client findClientById(Long clientId) {
    return clientRepository.findById(clientId)
      .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
  }

  public List<Item> validateAndConvertItems(List<ItemRequestDTO> itemRequestDTO) {
      if (itemRequestDTO == null || itemRequestDTO.isEmpty()) {
        throw new IllegalArgumentException("El pedido debe tener al menos un item.");
      }
      return itemRequestDTO.stream()
        .map(this::convertAndValidateItem)
        .collect(Collectors.toList());
  }
  public Item convertAndValidateItem(ItemRequestDTO itemDTO) {
    Dish dish = dishService.findDishByNameAndRestaurantAndMenu(itemDTO.getName(), itemDTO.getRestaurantId(), itemDTO.getMenuId());
    if(dish == null){
      throw new RuntimeException("El plato con nombre " + itemDTO.getName() + " no existe en el restaurante " + itemDTO.getRestaurantId() + " y menu " + itemDTO.getMenuId());
    }
    Item item = ItemDtoConverter.convertToEntity(itemDTO);
    item.setDish(dish);
    item.setQuantity(itemDTO.getQuantity());
    return item;
  }
  public Dish findDishByName(String name) {
    Dish dish = dishService.findDishByName(name);
    if (dish == null) {
      throw new RuntimeException("El plato con nombre " + name + " no existe");
    }
    return dish;
  }

  public Orden createAndSaveOrden(OrdenRequestDTO ordenRequestDTO, LocalDateTime dateOrder, StatusOrden statusOrder, Client client, List<Item> items, Double priceTotal) {
    List<Orden> existingOrders = ordenRepository.findByClientAndDateOrder(client.getId(), dateOrder);
    if (!existingOrders.isEmpty()) {
      throw new RuntimeException("La orden ya existe.");
    }
    Orden orden = IOrdenFactory.createOrden(ordenRequestDTO.getPriceTotal(), dateOrder, statusOrder, client, items);
    orden.setPriceTotal(priceTotal);
    items.forEach(item -> setItemOrdenAndDish(item, orden));
    return ordenRepository.save(orden);
  }
  public void setItemOrdenAndDish(Item item, Orden orden) {
    item.setOrden(orden);
    item.setDish(dishService.findDishByNameAndRestaurantAndMenu(item.getName(), item.getRestaurantId(), item.getMenuId()));
    item.setQuantity(item.getQuantity());
  }
  public Double calculateTotalPrice(List<Item> items) {
    return items.stream()
      .mapToDouble(item -> item.getPrice() * item.getQuantity())
      .sum();
  }

  public Double applyDiscount(Double priceTotal, Double discountPercentage) {
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

    if (ordenRequestDTO.getItems() != null && !ordenRequestDTO.getItems().isEmpty()) {
      List<Item> items = validateAndConvertItems(ordenRequestDTO.getItems());

      adjustItemPrices(items);
      orden.getItems().clear();
      orden.setItems(items);
      orden.setPriceTotal(calculateTotalPrice(items));
    }
    orden.setStatusOrder(ordenRequestDTO.getStatusOrder());
    orden.setClient(client);
    Orden updatedOrden = ordenRepository.save(orden);
    return OrdenDtoConverter.convertToResponseDTO(updatedOrden);
  }

  public void deleteOrden(Long id) {
    ordenRepository.deleteById(id);
  }
  public void notifyDishObserversForItems(List<Item> items) {
    items.forEach(item -> {
      Dish dish = item.getDish();
      if (dish != null) {
        dishService.notifyDishObservers(dish);
      }
    });
  }
  public void changeStateOrder(Long ordenId, StatusOrden newStatus) {
    Orden orden = ordenRepository.findById(ordenId).orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    IStatusOrdenStrategy strategy = statusStrategy.get(newStatus);
    if (strategy == null) {
     throw new IllegalArgumentException("Estado no reconocido: " + newStatus);
    }
    orden.setStatusOrdenStrategy(strategy);
    orden.handleStatus();
    ordenRepository.save(orden);
  }
  private void adjustItemPrices(List<Item> items) {
    items.forEach(item -> {
      Dish dish = dishService.findDishByNameAndRestaurantAndMenu(item.getName(), item.getRestaurantId(), item.getMenuId());
      if (dish != null) {
        dishService.updateObserver(dish);
        notifyDishObserversForItems(items);
        item.setDish(dish);
        if (dish.getPopular()) {
          dish.setPrice(dish.getPrice());
        }else { item.setPrice(dish.getPrice()); }
      }
    });
  }
}
