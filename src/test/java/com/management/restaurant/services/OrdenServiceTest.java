package com.management.restaurant.services;

import com.management.restaurant.DTO.ordens.*;
import com.management.restaurant.enums.StatusOrden;
import com.management.restaurant.factories.IOrdenFactory;
import com.management.restaurant.models.client.Client;
import com.management.restaurant.models.order.Item;
import com.management.restaurant.models.order.Orden;
import com.management.restaurant.models.restaurant.Dish;
import com.management.restaurant.models.restaurant.MenuRestaurant;
import com.management.restaurant.repositories.ClientRepository;
import com.management.restaurant.repositories.ItemRepository;
import com.management.restaurant.repositories.OrdenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrdenServiceTest {

    @Mock
    private OrdenRepository ordenRepository;
    @Mock
    private IOrdenFactory iordenFactory;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private DishService dishService;
    @Mock
    private ClientService clientService;
    @Spy
    @InjectMocks
    private OrdenService ordenService;
    private OrdenRequestDTO ordenRequestDTO;
    private Orden orden;
    private Client client;
    private Dish dish;
    private Item item;

    @BeforeEach
    void setUp() {
      MockitoAnnotations.openMocks(this);
      ordenRequestDTO = new OrdenRequestDTO();
      ordenRequestDTO.setClientId(1L);
      ordenRequestDTO.setItems(List.of(new ItemRequestDTO("Dish 1", 20.0, 2, 1L, 2L)));

      client = new Client();
      client.setId(1L);
      client.setIsFrecuent(true);

      dish = new Dish();
      dish.setName("Dish 1");
      dish.setPrice(20.0);
      dish.setPopular(true);

      item = new Item();
      item.setName("Dish 1");
      item.setQuantity(2);
      item.setDish(dish);
      item.setPrice(dish.getPrice());
      item.setRestaurantId(1L);
      item.setMenuId(2L);

      orden = new Orden(1L, null, LocalDateTime.now(), StatusOrden.PENDING, client, Collections.singletonList(item));
      ordenService = new OrdenService(ordenRepository, iordenFactory, clientRepository, clientService, dishService);
    }

    @Test
    @DisplayName("Traer orden por id")
    void getOrdenById() {
      orden.setItems(new ArrayList<>(Collections.singletonList(item)));
      orden.setPriceTotal(40.0);
      when(ordenRepository.findById(any(Long.class))).thenReturn(Optional.of(orden));
      OrdenResponseDTO response = ordenService.getOrdenById(1L);
      assertNotNull(response);
      assertEquals(40.0, response.getPriceTotal());

      verify(ordenRepository, times(1)).findById(1L);
    }
    @Test
    @DisplayName("Traer todas las ordenes")
    void getAllOrdenes() {
      List<Item> items = Collections.singletonList(item);
      orden.setItems(items);
      orden.setPriceTotal(40.0);
      when(ordenRepository.findAll()).thenReturn(Arrays.asList(orden));
      List<OrdenResponseDTO> responseList = ordenService.getAllOrdenes();
      assertNotNull(responseList);
      assertFalse(responseList.isEmpty());
      assertEquals(40.0, responseList.get(0).getPriceTotal());
      verify(ordenRepository, times(1)).findAll();
    }
  @Test
  @DisplayName("Caso negativo no devuelve las ordenes")
  void getAllOrdenesException() {
      when(ordenRepository.findAll()).thenThrow(new RuntimeException("Mock Exception"));
       RuntimeException exception = assertThrows(RuntimeException.class, () -> {
         ordenService.getAllOrdenes();
       });
       assertEquals("Error al obtener todas las órdenes", exception.getMessage());
    }

  @Test
  @DisplayName("Eliminar todas las ordenes")
  void deleteOrden() {
    doNothing().when(ordenRepository).deleteById(any(Long.class));

    ordenService.deleteOrden(1L);

    verify(ordenRepository, times(1)).deleteById(1L);
  }
  @Test
  @DisplayName("Cambiar el estado de las ordenes")
  void changeStateOrder() {
    when(ordenRepository.findById(any(Long.class))).thenReturn(Optional.of(orden));
    when(ordenRepository.save(any(Orden.class))).thenReturn(orden);
    ordenService.changeStateOrder(1L, StatusOrden.COMPLETED);
    verify(ordenRepository, times(1)).findById(1L);
    verify(ordenRepository, times(1)).save(orden);
  }
  @Test
  void testChangeStateOrder_StatusNotRecognized() {
    when(ordenRepository.findById(any(Long.class))).thenReturn(Optional.of(orden));
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      ordenService.changeStateOrder(1L, null);
    });
    assertTrue(exception.getMessage().contains("Estado no reconocido"));
    verify(ordenRepository, times(1)).findById(1L);
    }

  @Test
  @DisplayName("Encontrar plato por nombre")
  void findDishByNameExist() {
      Dish mockDish = new Dish();
      mockDish.setName("Dish 1");
      mockDish.setPrice(20.0);
      when(dishService.findDishByName("Dish 1")).thenReturn(mockDish);
      Dish dish = ordenService.findDishByName("Dish 1");
      assertNotNull(dish); assertEquals("Dish 1", dish.getName());
      verify(dishService, times(1)).findDishByName("Dish 1");
    }
  @Test
  @DisplayName("Caso negativo, no encuentra el plato")
  void findDishByNameThrowsException() {
      when(dishService.findDishByName(any(String.class))).thenReturn(null);
      RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        ordenService.findDishByName("Dish 1"); });
      assertEquals("El plato con nombre Dish 1 no existe", exception.getMessage());

      verify(dishService, times(1)).findDishByName("Dish 1");
    }

  @Test
  @DisplayName("Nofificacion de los observadores de platos")
  void notifyDishObserversForItems() {
      Dish mockDish = new Dish();
      mockDish.setName("Dish 1");
      mockDish.setPrice(20.0);

      Item item1 = new Item();
      item1.setName("Item 1");
      item1.setDish(mockDish);

      Item item2 = new Item();
      item2.setName("Item 2");
      item2.setDish(null);

      List<Item> items = Arrays.asList(item1, item2);
      doNothing().when(dishService).notifyDishObservers(any(Dish.class));
      ordenService.notifyDishObserversForItems(items);
      verify(dishService, times(1)).notifyDishObservers(mockDish);
    }
  @Test
  @DisplayName("Encuentra cliente por id")
  void findClientByIdFound() {
      Client mockClient = new Client();
      mockClient.setId(1L);
      when(clientRepository.findById(any(Long.class))).thenReturn(Optional.of(mockClient));
      Client client = ordenService.findClientById(1L);

      assertNotNull(client);
      assertEquals(1L, client.getId());
      verify(clientRepository, times(1)).findById(1L);
    }

  @Test
  @DisplayName("No encuentra cliente por id")
  void findClientByIdNotFound() {
     when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());
     RuntimeException exception = assertThrows(RuntimeException.class, () -> {
       ordenService.findClientById(1L); });
     assertEquals("Cliente no encontrado", exception.getMessage());
     verify(clientRepository, times(1)).findById(1L);
    }
  @Test
  @DisplayName("Contiene a item orden y plato")
  void setItemOrdenAndDish() {
      Orden orden = new Orden();
      Dish mockDish = new Dish();
      mockDish.setName("Dish 1");
      mockDish.setPrice(20.0);

    MenuRestaurant mockMenuRestaurant = new MenuRestaurant();
    mockMenuRestaurant.setIdMenu(1L);
    mockDish.setMenuRestaurant(mockMenuRestaurant);

      Item item = new Item();
      item.setName("Dish 1");
      item.setRestaurantId(1L);
      item.setMenuId(1L);
      when(dishService.findDishByNameAndRestaurantAndMenu(item.getName(), item.getRestaurantId(), item.getMenuId())).thenReturn(mockDish);
      ordenService.setItemOrdenAndDish(item, orden);

      assertEquals(orden, item.getOrden());
      assertEquals(mockDish, item.getDish());
      assertEquals("Dish 1", mockDish.getName());
      assertEquals(1L, item.getRestaurantId());
      assertEquals(1L, item.getDish().getMenuRestaurant().getIdMenu());
      verify(dishService, times(1)).findDishByNameAndRestaurantAndMenu("Dish 1", 1L, 1L);
    }
  @Test
  @DisplayName("Calcula el precio total")
  void calculateTotalPrice() {
      Item item1 = new Item();
      item1.setPrice(10.0);
      item1.setQuantity(2);

      Item item2 = new Item();
      item2.setPrice(20.0);
      item2.setQuantity(1);
      List<Item> items = Arrays.asList(item1, item2);
      Double totalPrice = ordenService.calculateTotalPrice(items);
      assertEquals(40.0, totalPrice);
    }
  @Test
  @DisplayName("Aplica descuento")
  void applyDiscount() {
      Double priceTotal = 100.0;
      Double discountPercentage = 20.0;
      Double discountedPrice = ordenService.applyDiscount(priceTotal, discountPercentage);
      assertEquals(80.0, discountedPrice);
    }
  @Test
  @DisplayName("valida items")
  void convertAndValidateItem() {
    ItemRequestDTO itemRequestDTO = new ItemRequestDTO("Dish 1", 20.0, 2, 1L, 2L);
    when(dishService.findDishByNameAndRestaurantAndMenu("Dish 1", 1L, 2L)).thenReturn(dish);
    Item item = ordenService.convertAndValidateItem(itemRequestDTO);
    assertEquals(dish, item.getDish());
    verify(dishService, times(1)).findDishByNameAndRestaurantAndMenu("Dish 1", 1L, 2L);
    }

  @Test
  @DisplayName("Valida que la lista de item no este vacia")
  void validateAndConvertItemsNullOrEmpty() {
      IllegalArgumentException nullException = assertThrows(IllegalArgumentException.class, () -> {
        ordenService.validateAndConvertItems(null);
      });
      assertEquals("El pedido debe tener al menos un item.", nullException.getMessage());
      IllegalArgumentException emptyException = assertThrows(IllegalArgumentException.class, () -> {
        ordenService.validateAndConvertItems(Collections.emptyList()); });
      assertEquals("El pedido debe tener al menos un item.", emptyException.getMessage());
    }
  @Test
  @DisplayName("Valida que la lista de item")
  void validateAndConvertItemsValidList() {
      ItemRequestDTO itemRequestDTO = new ItemRequestDTO("Dish 1", 20.0, 2, 1L, 2L);
      List<ItemRequestDTO> itemRequestDTOList = Collections.singletonList(itemRequestDTO);
    when(dishService.findDishByNameAndRestaurantAndMenu(any(String.class), any(Long.class), any(Long.class))).thenReturn(dish);
      List<Item> items = ordenService.validateAndConvertItems(itemRequestDTOList);
      assertNotNull(items);
      assertEquals(1, items.size());
      Item item = items.get(0); assertEquals(dish, item.getDish());
    verify(dishService, times(1)).findDishByNameAndRestaurantAndMenu("Dish 1", 1L, 2L);
    }
  @Test
  @DisplayName("Actualizar orden")
  void updateOrdenSuccess() {
    Long ordenId = 1L;
    when(ordenRepository.findById(ordenId)).thenReturn(Optional.of(orden));
    when(clientRepository.findById(ordenRequestDTO.getClientId())).thenReturn(Optional.of(client));
    when(dishService.findDishByNameAndRestaurantAndMenu(anyString(), anyLong(), anyLong())).thenReturn(dish);

    AtomicLong idCounter = new AtomicLong(1);
    List<Item> items = ordenRequestDTO.getItems().stream()
      .map(itemDTO -> {
        Item item = new Item();
        item.setId(idCounter.getAndIncrement());
        item.setName(itemDTO.getName());
        item.setQuantity(itemDTO.getQuantity());
        item.setPrice(itemDTO.getPrice());
        item.setRestaurantId(itemDTO.getRestaurantId());
        item.setMenuId(itemDTO.getMenuId());
        item.setDish(dish);
        item.setOrden(orden);
        return item;
      })
      .collect(Collectors.toList());
    orden.setItems(items);
    Double initialPriceTotal = 40.0;
    orden.setPriceTotal(initialPriceTotal);
    when(ordenRepository.save(any(Orden.class))).thenReturn(orden);

    items.forEach(item -> when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item)));

    Double expectedPriceTotal = initialPriceTotal;
    if (client.getIsFrecuent()) {
      expectedPriceTotal = ordenService.applyDiscount(expectedPriceTotal, 2.38);
    }
    OrdenResponseDTO response = ordenService.updateOrden(ordenId, ordenRequestDTO);
    assertNotNull(response);
    assertEquals(expectedPriceTotal, response.getPriceTotal());

    verify(ordenRepository, times(1)).findById(ordenId);
    verify(clientRepository, times(1)).findById(ordenRequestDTO.getClientId());
    verify(dishService, times(3)).findDishByNameAndRestaurantAndMenu(anyString(), anyLong(), anyLong());
    verify(ordenRepository, times(1)).save(orden);

    assertEquals(orden.getItems().size(), items.size()); orden.getItems().forEach(item -> {
      assertTrue(item.getQuantity() > 0);
      assertNotNull(item.getPrice());
      assertNotNull(item.getDish());
    });
    }
  @Test @DisplayName("Ajustar precios de ítems - Plato Popular")
  void adjustItemPricesPopularDish() {
      Dish popularDish = new Dish();
      popularDish.setName("Dish 1");
      popularDish.setPrice(20.0);
      popularDish.setPopular(true);

      Item popularItem = new Item();
      popularItem.setName("Dish 1");
      popularItem.setQuantity(2);
      popularItem.setDish(popularDish);
      popularItem.setPrice(popularDish.getPrice());
      popularItem.setRestaurantId(1L);
      popularItem.setMenuId(2L);
      List<Item> items = List.of(popularItem);

      doNothing().when(dishService).updateObserver(eq(popularDish));
      when(dishService.findDishByNameAndRestaurantAndMenu(eq("Dish 1"), eq(1L), eq(2L))).thenReturn(popularDish);

      ordenService.adjustItemPrices(items);
      assertEquals(20.0, popularItem.getPrice());
      assertEquals(20.0, popularDish.getPrice());
      verify(dishService, times(1)).updateObserver(eq(popularDish));
      verify(dishService, times(1)).findDishByNameAndRestaurantAndMenu("Dish 1", 1L, 2L);
    }
  @Test
  @DisplayName("Ajustar precios de items, Plato no Popular")
  void adjustItemPricesNonPopularDish() {

    Dish nonPopularDish = new Dish();
    nonPopularDish.setName("Dish 1");
    nonPopularDish.setPrice(20.0);
    nonPopularDish.setPopular(false);

    Item nonPopularItem = new Item();
    nonPopularItem.setName("Dish 1");
    nonPopularItem.setQuantity(2);
    nonPopularItem.setDish(nonPopularDish);
    nonPopularItem.setPrice(nonPopularDish.getPrice());
    nonPopularItem.setRestaurantId(1L);
    nonPopularItem.setMenuId(2L);

    List<Item> items = List.of(nonPopularItem);

    doNothing().when(dishService).updateObserver(eq(nonPopularDish));
    when(dishService.findDishByNameAndRestaurantAndMenu(eq("Dish 1"), eq(1L), eq(2L))).thenReturn(nonPopularDish);

    ordenService.adjustItemPrices(items);

    assertEquals(20.0, nonPopularItem.getPrice());
    verify(dishService, times(1)).updateObserver(eq(nonPopularDish));
    verify(dishService, times(1)).findDishByNameAndRestaurantAndMenu("Dish 1", 1L, 2L);
  }
  @Test
  @DisplayName("Crear y guardar orden")
  void createAndSaveOrden() {
    LocalDateTime dateOrder = LocalDateTime.now();
    StatusOrden statusOrder = StatusOrden.PENDING;

    when(ordenRepository.findByClientAndDateOrder(anyLong(), any(LocalDateTime.class))).thenReturn(Collections.emptyList());
    when(iordenFactory.createOrden(eq(0.0), eq(dateOrder), eq(statusOrder), eq(client), anyList())).thenAnswer(invocation -> {
    Long ordenId = 1L;
    LocalDateTime orderDate = invocation.getArgument(1);
    StatusOrden orderStatus = invocation.getArgument(2);
    Client orderClient = invocation.getArgument(3);
    List<Item> orderItems = invocation.getArgument(4);
    Double initialPriceTotal = invocation.getArgument(0);

      return new Orden(ordenId, 0.0, orderDate, orderStatus, orderClient, new ArrayList<>(orderItems));
    });
    when(ordenRepository.save(any(Orden.class))).thenAnswer(invocation -> {
      Orden savedOrden = invocation.getArgument(0);
      savedOrden.setPriceTotal(40.0);
      return savedOrden;
    });
    Orden result = ordenService.createAndSaveOrden(ordenRequestDTO, dateOrder, statusOrder, client, List.of(item));
    assertNotNull(result);
    assertEquals(40.0, result.getPriceTotal());
    assertEquals(1, result.getItems().size());
    assertEquals(client, result.getClient());

    verify(ordenRepository, times(1)).findByClientAndDateOrder(client.getId(), dateOrder);
    verify(iordenFactory, times(1)).createOrden(anyDouble(), eq(dateOrder), eq(statusOrder), eq(client), anyList());
    verify(ordenRepository, times(1)).save(any(Orden.class));
    }


  @Test
  @DisplayName("Crear orden")
  void createOrden() {
    when(clientRepository.findById(any(Long.class))).thenReturn(Optional.of(client));
    when(dishService.findDishByNameAndRestaurantAndMenu(any(String.class), any(Long.class), any(Long.class))).thenReturn(dish);
    when(iordenFactory.createOrden(eq(0.0), any(LocalDateTime.class), any(StatusOrden.class), any(Client.class), anyList()))
      .thenAnswer(invocation -> {
        Double pTotal = invocation.getArgument(0);
        LocalDateTime orderDate = invocation.getArgument(1);
        StatusOrden orderStatus = invocation.getArgument(2);
        Client orderClient = invocation.getArgument(3);
        List<Item> orderItems = invocation.getArgument(4);
        return new Orden(1L, pTotal, orderDate, orderStatus, orderClient, new ArrayList<>(orderItems));
      });
    doNothing().when(clientService).updateObserver(any(Client.class));
    doNothing().when(clientService).notifyClientObservers(any(Client.class));
    doNothing().when(dishService).updateObserver(any(Dish.class));

    when(ordenRepository.save(any(Orden.class))).thenReturn(orden);
    OrdenResponseDTO response = ordenService.createOrden(ordenRequestDTO);
    assertNotNull(response);
    Double expectedPriceTotal = 40.0 - (40.0 * 2.38 / 100);
    assertEquals(expectedPriceTotal, response.getPriceTotal());
    assertEquals(2, response.getItems().get(0).getQuantity());

    verify(clientRepository, times(1)).findById(1L);
    verify(dishService, times(3)).findDishByNameAndRestaurantAndMenu("Dish 1", 1L, 2L);
    verify(iordenFactory, times(1)).createOrden(eq(0.0), any(LocalDateTime.class), any(StatusOrden.class), any(Client.class), anyList());
    verify(clientService, times(1)).updateObserver(client);
    verify(clientService, times(1)).notifyClientObservers(client);
    verify(dishService, times(1)).updateObserver(dish);
    verify(ordenRepository, times(1)).save(orden);
  }
  @Test
  @DisplayName("Crear orden - Manejo de excepción")
  void createOrdenExceptionHandling() {
      when(clientRepository.findById(any(Long.class))).thenThrow(new RuntimeException("Mock Exception"));
      RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        ordenService.createOrden(ordenRequestDTO);
      });
      assertEquals("Error al crear la orden: Mock Exception", exception.getMessage());
    }
  @Test
  @DisplayName("Convertir y validar item - Manejo de excepción")
  void convertAndValidateItemException() {
      when(dishService.findDishByNameAndRestaurantAndMenu(any(String.class), any(Long.class), any(Long.class)))
        .thenReturn(null);
      RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        ordenService.convertAndValidateItem(new ItemRequestDTO("Dish 1", 20.0, 2, 1L, 2L)); });
      assertEquals("El plato con nombre Dish 1 no existe en el restaurante 1 y menu 2", exception.getMessage());
    }

}
