package com.management.restaurant.services;

import com.management.restaurant.models.restaurant.Restaurant;
import com.management.restaurant.repositories.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalTime;
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

class RestaurantServiceTest {

  private RestaurantRepository restaurantRepository;
  private RestaurantService restaurantService;
  private Restaurant restaurant;

  @BeforeEach
  void setUp() {
    restaurantRepository = mock(RestaurantRepository.class);
    restaurantService = new RestaurantService(restaurantRepository);
    restaurant = new Restaurant();
    restaurant.setName("El sazon");
    restaurant.setAddress("centro");
    restaurant.setPhoneNumber("345678322");
    restaurant.setOpeningHours(LocalTime.of(9, 0));
    restaurant.setClosingHours(LocalTime.of(22, 0));
  }

  @Test
  @DisplayName("Encontrar retaurant con menu")
  void addRestaurant() {
    when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
    Restaurant result = restaurantService.addRestaurant(restaurant);

    assertNotNull(result);
    assertEquals(restaurant, result);

    verify(restaurantRepository, times(1)).save(any(Restaurant.class));
  }

  @Test
  @DisplayName("Encontrar retaurant con menu")
  void getRestaurantWithMenu() {

    when(restaurantRepository.findMenuByRestaurant(anyLong())).thenReturn(Optional.of(restaurant));

    Restaurant result = restaurantService.getRestaurantWithMenu(1L);

    assertNotNull(result);
    assertEquals(restaurant.getId(), result.getId());
    assertEquals(restaurant.getName(), result.getName());
    verify(restaurantRepository).findMenuByRestaurant(anyLong());
    verify(restaurantRepository, never()).findById(anyLong());
  }
  @Test
  @DisplayName("Encontrar retaurant con findByid")
  void getRestaurantWithMenuUsingFindById() {
    when(restaurantRepository.findMenuByRestaurant(anyLong())).thenReturn(Optional.empty());
    when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
    Restaurant result = restaurantService.getRestaurantWithMenu(1L);
    assertNotNull(result);
    assertEquals(restaurant.getId(), result.getId());
    assertEquals(restaurant.getName(), result.getName());
    verify(restaurantRepository).findMenuByRestaurant(anyLong());
    verify(restaurantRepository).findById(anyLong());
  }
  @Test
  @DisplayName("Eliminar restaurante satisfactoriamente")
  void deleteRestaurant() {
    when(restaurantRepository.existsById(anyLong())).thenReturn(true);
    doNothing().when(restaurantRepository).deleteById(anyLong());
    assertDoesNotThrow(() -> restaurantService.deleteRestaurant(1L));

    verify(restaurantRepository).existsById(anyLong());
    verify(restaurantRepository).deleteById(anyLong());
  }
  @Test
  @DisplayName("Restaurante no encontrado, no puede eliminarse.")
  void notDeleteRestaurant() {
    when(restaurantRepository.existsById(anyLong())).thenReturn(false);
    Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
      restaurantService.deleteRestaurant(1L); });
    assertEquals("Restaurante no encontrado con ID: 1", exception.getMessage());
    verify(restaurantRepository).existsById(anyLong());
    verify(restaurantRepository, never()).deleteById(anyLong());
  }
}