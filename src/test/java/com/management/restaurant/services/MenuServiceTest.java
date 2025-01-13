package com.management.restaurant.services;


import com.management.restaurant.DTO.restaurant.DishRequestDTO;
import com.management.restaurant.DTO.restaurant.MenuResquetDTO;
import com.management.restaurant.models.restaurant.Dish;
import com.management.restaurant.models.restaurant.MenuRestaurant;
import com.management.restaurant.models.restaurant.Restaurant;
import com.management.restaurant.repositories.DishRepository;
import com.management.restaurant.repositories.ItemRepository;
import com.management.restaurant.repositories.MenuRepository;
import com.management.restaurant.repositories.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class MenuServiceTest {

  private DishRepository dishRepository;
  private MenuRepository menuRepository;
  private RestaurantRepository restaurantRepository;
  private ItemRepository itemRepository;
  private MenuService menuService;
  Restaurant  restaurant;
  MenuRestaurant menu;

  @BeforeEach
  void setUp() {
    dishRepository = mock(DishRepository.class);
    menuRepository = mock(MenuRepository.class);
    restaurantRepository = mock(RestaurantRepository.class);
    itemRepository = mock(ItemRepository.class);
    menuService = new MenuService( menuRepository, dishRepository, restaurantRepository, itemRepository);
    restaurant = new Restaurant(1L, "Restaurante Test", "123 Main St", "555-1234", null, null, null);
    menu = new MenuRestaurant(1L, "Menu 1", restaurant, List.of());

  }
  @Test
  @DisplayName("Agregar Menu")
  void addMenu() {
    MenuResquetDTO menuRequestDTO = new MenuResquetDTO();
    menuRequestDTO.setDescription("Menu 1");
    menuRequestDTO.setRestaurantId(1L);
    menuRequestDTO.setDishes(List.of());

    when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
    when(menuRepository.findByRestaurant_Id(anyLong())).thenReturn(Optional.empty());
    when(menuRepository.save(any(MenuRestaurant.class))).thenReturn(menu);

    MenuRestaurant result = menuService.addMenu(menuRequestDTO);

    assertNotNull(result); assertEquals(menu.getIdMenu(), result.getIdMenu());
    assertEquals(menu.getDescription(), result.getDescription());

    verify(restaurantRepository, times(1)).findById(anyLong());
    verify(menuRepository, times(1)).findByRestaurant_Id(anyLong());
    verify(menuRepository, times(1)).save(any(MenuRestaurant.class));
  }
  @Test
  @DisplayName("Caso negativo, cuando no puede agregar menu porque el id del restaurante no existe")
  void addMenuRestaurantNotFound() {
    MenuResquetDTO menuRequestDTO = new MenuResquetDTO();
    menuRequestDTO.setDescription("Menu 1");
    menuRequestDTO.setRestaurantId(1L);
    menuRequestDTO.setDishes(List.of());

    when(restaurantRepository.findById(anyLong())).thenReturn(Optional.empty());
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> { menuService.addMenu(menuRequestDTO); });
    assertEquals("Restaurante no encontrado", exception.getMessage());

    verify(restaurantRepository, times(1)).findById(anyLong());
    verify(menuRepository, times(0)).findByRestaurant_Id(anyLong());
    verify(menuRepository, times(0)).save(any(MenuRestaurant.class)); }

  @Test
  @DisplayName("Agregar menu - Restaurante ya tiene un menu asociado")
  void addMenuRestaurantAlreadyHasMenu() {

    MenuResquetDTO menuRequestDTO = new MenuResquetDTO();
    menuRequestDTO.setDescription("Menu 1");
    menuRequestDTO.setRestaurantId(1L);
    menuRequestDTO.setDishes(List.of());

    MenuRestaurant existingMenu = new MenuRestaurant(1L, "Existing Menu", restaurant, List.of());
    when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
    when(menuRepository.findByRestaurant_Id(anyLong())).thenReturn(Optional.of(existingMenu));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> { menuService.addMenu(menuRequestDTO); });

    assertEquals("El restaurante ya tiene un menu asociado.", exception.getMessage());
    verify(restaurantRepository, times(1)).findById(anyLong());
    verify(menuRepository, times(1)).findByRestaurant_Id(anyLong());
    verify(menuRepository, times(0)).save(any(MenuRestaurant.class));

  }

  @Test
  @DisplayName("Encontrar Menu con id del restaurante")
  void findMenuByRestaurantId() {

    when(menuRepository.findByRestaurant_Id(anyLong())).thenReturn(Optional.of(menu));
    MenuRestaurant result = menuService.findMenuByRestaurantId(1L);

    assertNotNull(result);
    assertEquals(menu.getIdMenu(), result.getIdMenu());
    assertEquals(menu.getDescription(), result.getDescription());
    verify(menuRepository, times(1)).findByRestaurant_Id(anyLong());
  }

  @Test
  @DisplayName("Actualizar menu")
  void updateMenu() {
    DishRequestDTO dishDTO = new DishRequestDTO();
    dishDTO.setName("Pasta");
    dishDTO.setPrice(15.99);
    dishDTO.setPopular(true);

    MenuResquetDTO menuRequestDTO = new MenuResquetDTO();
    menuRequestDTO.setDescription("Menu Updated");
    menuRequestDTO.setRestaurantId(1L);
    menuRequestDTO.setDishes(List.of(dishDTO));

      when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
      when(menuRepository.findByRestaurant_Id(anyLong())).thenReturn(Optional.of(menu));
      when(menuRepository.save(any(MenuRestaurant.class))).thenReturn(menu);
      MenuRestaurant result = menuService.updateMenu(menuRequestDTO);

      assertNotNull(result);
      assertEquals("Menu Updated", result.getDescription());

      verify(restaurantRepository, times(1)).findById(anyLong());
      verify(menuRepository, times(1)).findByRestaurant_Id(anyLong());
      verify(menuRepository, times(1)).save(any(MenuRestaurant.class));
      verify(dishRepository, times(1)).saveAll(anyList());
  }

  @Test
  @DisplayName("Eliminar Menu")
  void deleteMenu() {
    when(menuRepository.findByRestaurant_Id(anyLong())).thenReturn(Optional.of(menu));
    menuService.deleteMenu(1L);
    verify(dishRepository, times(menu.getDishes().size())).delete(any(Dish.class));
    verify(menuRepository, times(1)).delete(menu);
  }
  @Test @DisplayName("Caso negativo de Eliminar menu - Menu no encontrado")
  void deleteMenuNotFound() {
    when(menuRepository.findByRestaurant_Id(anyLong())).thenReturn(Optional.empty());
    EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
      menuService.deleteMenu(1L);
    });
    assertEquals("Menu no encontrado con id restaurante: 1", exception.getMessage());
    verify(menuRepository, times(1)).findByRestaurant_Id(anyLong());
    verify(dishRepository, times(0)).delete(any(Dish.class));
    verify(menuRepository, times(0)).delete(any(MenuRestaurant.class)); }
}