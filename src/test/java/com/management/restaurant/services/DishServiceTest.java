package com.management.restaurant.services;


import com.management.restaurant.DTO.ordens.DishDTO;
import com.management.restaurant.DTO.restaurant.DishRequestDTO;
import com.management.restaurant.models.client.Client;
import com.management.restaurant.models.restaurant.Dish;
import com.management.restaurant.models.restaurant.MenuRestaurant;
import com.management.restaurant.models.restaurant.Restaurant;
import com.management.restaurant.repositories.DishRepository;
import com.management.restaurant.repositories.ItemRepository;
import com.management.restaurant.repositories.MenuRepository;
import com.management.restaurant.services.observer.ObserverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class DishServiceTest {

  private DishRepository dishRepository;
  private MenuRepository menuRepository;
  private MenuRestaurant menuRestaurant;
  private ObserverManager<Dish> observerManager;
  private DishService dishService;
  private DishRequestDTO dishRequestDTO;
  private ItemRepository itemRepository;

  private Dish existingDish;


  @BeforeEach
  void setUp() {
    dishRepository = mock(DishRepository.class);
    itemRepository = mock(ItemRepository.class);
    menuRepository = mock(MenuRepository.class);
    observerManager = mock(ObserverManager.class);
    menuRestaurant = mock(MenuRestaurant.class);

    dishService = new DishService(observerManager, dishRepository, menuRepository, itemRepository);
    Restaurant restaurant = new Restaurant(1L, "Restaurante Test", "123 Main St", "555-1234", LocalTime.of(9, 0), LocalTime.of(17, 0), null);
    menuRestaurant = new MenuRestaurant(1L, "Menu 1", restaurant, List.of());
    existingDish = new Dish(1L, "Pasta", null, 12.99, false);

    dishRequestDTO = new DishRequestDTO();
    dishRequestDTO.setName("Pasta");
    dishRequestDTO.setPrice(15.99);
    dishRequestDTO.setPopular(false);
    dishRequestDTO.setMenuRestaurantId(menuRestaurant.getIdMenu());
  }

  @Test
  @DisplayName("buscar todos los platos")
  void getAllDish() {
    List<Dish> dishes = List.of(existingDish);
    when(dishRepository.findAllDishes()).thenReturn(dishes);
    List<Dish> result = dishService.getAllDish();
    assertEquals(1, result.size());
    verify(dishRepository, times(1)).findAllDishes();
  }

  @Test
  @DisplayName("Agregar plato")
  void createDish() {

    Dish newDish = new Dish();
    newDish.setName(dishRequestDTO.getName());
    newDish.setPrice(dishRequestDTO.getPrice());
    newDish.setPopular(dishRequestDTO.getPopular());
    newDish.setMenuRestaurant(menuRestaurant);

    when(menuRepository.findById(dishRequestDTO.getMenuRestaurantId())).thenReturn(Optional.of(menuRestaurant));
    when(dishRepository.save(any(Dish.class))).thenReturn(newDish);

    Dish savedDish = dishService.createDish(dishRequestDTO);
    assertNotNull(savedDish);
    assertEquals("Pasta", savedDish.getName());
    assertEquals(15.99, savedDish.getPrice());
    assertFalse(savedDish.getPopular());
    assertEquals(menuRestaurant,savedDish.getMenuRestaurant());
    verify(dishRepository).save(any(Dish.class));
  }

  @Test
  @DisplayName("Actualizar plato")
  void updateDish() {

    DishRequestDTO updateRequestDTO = new DishRequestDTO();
    updateRequestDTO.setName("Pasta Updated");
    updateRequestDTO.setPrice(15.99);
    updateRequestDTO.setPopular(true);
    updateRequestDTO.setMenuRestaurantId(menuRestaurant.getIdMenu());

    when(dishRepository.findById(anyLong())).thenReturn(Optional.of(existingDish));
    when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menuRestaurant));
    when(dishRepository.save(any(Dish.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Dish result = dishService.updateDish(existingDish.getId(), updateRequestDTO);
    assertNotNull(result);
    assertEquals("Pasta Updated", result.getName());
    assertEquals(15.99, result.getPrice());
    assertTrue(result.getPopular());
    assertEquals(menuRestaurant, result.getMenuRestaurant());
    verify(dishRepository, times(1)).save(existingDish);
  }

  @Test
  @DisplayName("Eliminar plato")
  void deleteDish() {
    when(dishRepository.findById(anyLong())).thenReturn(Optional.of(existingDish)); dishService.deleteDish(existingDish.getId());
    verify(dishRepository, times(1)).deleteById(existingDish.getId());
    verify(observerManager, times(1)).removeObserver(dishService);
  }

  @Test
  @DisplayName("Encontrar plato por nombre")
  void findDishByName() {
    when(dishRepository.findByName(anyString())).thenReturn(existingDish);
    Dish result = dishService.findDishByName("Pasta");
    assertNotNull(result);
    assertEquals("Pasta", result.getName());
    verify(dishRepository, times(1)).findByName("Pasta");
  }

  @Test
  @DisplayName("Agregar observador de plato")
  void addDishObserver() {
    dishService.addDishObserver();
    verify(observerManager, times(2)).addObserver(dishService);
  }

  @Test
  @DisplayName("Eliminar observador de plato")
  void removeDishObserver() {
    dishService.removeDishObserver(dishService);
    verify(observerManager, times(1)).removeObserver(dishService);
  }

  @Test
  @DisplayName("Notificar a observadores de plato")
  void notifyDishObservers() {
    dishService.notifyDishObservers(existingDish);
    verify(observerManager, times(1)).notifyObservers(existingDish);
  }

  @Test
  @DisplayName("Actualizar observador de plato cuando el plato pase a ser popular")
  void updateObserver() {
    Dish dish = new Dish(1L, "Pasta", null, 12.99, false);
    when(itemRepository.countPopularDishes(dish.getId())).thenReturn(101L);
    when(dishRepository.save(any(Dish.class))).thenAnswer(invocation -> invocation.getArgument(0)); dishService.updateObserver(dish);

    assertTrue(dish.getPopular());
    assertEquals(12.99 * 1.0573, dish.getPrice(), 0.001);
    verify(dishRepository, times(1)).save(dish);

  }
}