package com.management.restaurant.controllers;

import com.management.restaurant.DTO.restaurant.RestaurantRequestDTO;
import com.management.restaurant.DTO.restaurant.RestaurantResponseDTO;
import com.management.restaurant.models.restaurant.Restaurant;
import com.management.restaurant.services.RestaurantService;
import com.management.restaurant.utils.RestaurantDtoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestaurantControllerTest {

  private RestaurantService restaurantService;
  private RestaurantController restaurantController;
  private WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    restaurantService = mock(RestaurantService.class);
    webTestClient = WebTestClient.bindToController(new RestaurantController(restaurantService)).build();
  }
  private RestaurantRequestDTO createRestaurantRequestDTO(){
    RestaurantRequestDTO restaurantRequestDTO = new RestaurantRequestDTO();
    restaurantRequestDTO.setName("El sazon");
    restaurantRequestDTO.setAddress("centro");
    restaurantRequestDTO.setPhoneNumber("238654786");
    restaurantRequestDTO.setOpeningHours(LocalTime.of(9, 0));
    restaurantRequestDTO.setClosingHours(LocalTime.of(22, 0));
    return restaurantRequestDTO;
  }
  private RestaurantResponseDTO createRestaurantResponseDTO(){
    RestaurantResponseDTO restaurantResponseDTO = new RestaurantResponseDTO();
    restaurantResponseDTO.setId(1L);
    restaurantResponseDTO.setName("El sazon");
    restaurantResponseDTO.setAddress("centro");
    restaurantResponseDTO.setPhoneNumber("238654786");
    restaurantResponseDTO.setOpeningHours(LocalTime.of(9, 0));
    restaurantResponseDTO.setClosingHours(LocalTime.of(22, 0));
    return restaurantResponseDTO;
  }
  private Restaurant createRestaurantMock() {
    Restaurant restaurant = new Restaurant();
    restaurant.setId(1L);
    restaurant.setName("El sazon");
    restaurant.setAddress("centro");
    restaurant.setPhoneNumber("238654786");
    restaurant.setOpeningHours(LocalTime.of(9, 0));
    restaurant.setClosingHours(LocalTime.of(22, 0));
    return restaurant;
  }
  @Test
  @DisplayName("Crear restaurante")
  void createRestaurant() {
    RestaurantRequestDTO restaurantRequestDTO = createRestaurantRequestDTO();
    Restaurant restaurant = RestaurantDtoConverter.convertToEntity(restaurantRequestDTO);
    restaurant.setId(1L);
    RestaurantResponseDTO restaurantResponseDTO = createRestaurantResponseDTO();

    when(restaurantService.addRestaurant(any(Restaurant.class))).thenReturn(restaurant);

    webTestClient.post()
      .uri("/api/restaurante")
      .bodyValue(restaurantRequestDTO)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody(RestaurantResponseDTO.class)
      .value(response -> {
        assertEquals(restaurantResponseDTO.getId(), response.getId());
        assertEquals(restaurantResponseDTO.getName(), response.getName());
        assertEquals(restaurantResponseDTO.getAddress(), response.getAddress());
        assertEquals(restaurantResponseDTO.getPhoneNumber(), response.getPhoneNumber());
        assertEquals(restaurantResponseDTO.getOpeningHours(), response.getOpeningHours());
        assertEquals(restaurantResponseDTO.getClosingHours(), response.getClosingHours());
      });

    verify(restaurantService).addRestaurant(any(Restaurant.class));
  }

  @Test
  @DisplayName("Obtener restaurant con menu por id")
  void getRestaurantWithMenu() {
    Restaurant restaurant = createRestaurantMock();
    RestaurantResponseDTO restaurantResponseDTO = createRestaurantResponseDTO();

    when(restaurantService.getRestaurantWithMenu(anyLong())).thenReturn(restaurant);

    webTestClient.get()
      .uri("/api/restaurante/{restaurantId}", 1L)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody(RestaurantResponseDTO.class)
      .value(response->{
        assertEquals(restaurantResponseDTO.getId(), response.getId());
        assertEquals(restaurantResponseDTO.getName(), response.getName());
        assertEquals(restaurantResponseDTO.getAddress(), response.getAddress());
        assertEquals(restaurantResponseDTO.getPhoneNumber(), response.getPhoneNumber());
        assertEquals(restaurantResponseDTO.getOpeningHours(), response.getOpeningHours());
       });
    verify(restaurantService).getRestaurantWithMenu(anyLong());
  }

  @Test
  @DisplayName("Eliminar restaurante exitosamente.")
  void deleteRestaurant() {
    webTestClient.delete()
      .uri("/api/restaurante/{id}", 1L)
      .exchange() .expectStatus().isOk()
      .expectBody(String.class)
      .isEqualTo("Restaurante eliminado con éxito.");
    verify(restaurantService).deleteRestaurant(anyLong());
  }
  @Test
  @DisplayName("No se encuentra el restaurante para eliminar.")
  void notdeleteRestaurant() {
    doThrow(new EmptyResultDataAccessException(1)).when(restaurantService).deleteRestaurant(anyLong());
    webTestClient.delete()
      .uri("/api/restaurante/{id}", 1L)
      .exchange()
      .expectStatus().isNotFound()
      .expectBody(String.class)
      .isEqualTo("Restaurante no encontrado.");
    verify(restaurantService).deleteRestaurant(anyLong());
  }
  @Test
  @DisplayName("No se puede eliminar restaurante por error con el servidor.")
  void notdeleteRestaurantServerError() {
    doThrow(new RuntimeException()).when(restaurantService).deleteRestaurant(anyLong());
    webTestClient.delete()
      .uri("/api/restaurante/{id}", 1L)
      .exchange()
      .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
      .expectBody(String.class)
      .isEqualTo("Error al eliminar el restaurante.");
    verify(restaurantService).deleteRestaurant(anyLong());
  }
  @Test
  @DisplayName("Obtener restaurante con menu, No encontrado")
  void getRestaurantWithMenu_NotFound() {
    when(restaurantService.getRestaurantWithMenu(anyLong())).thenReturn(null);

    webTestClient.get() .uri("/api/restaurante/{restaurantId}", 1L)
      .exchange()
      .expectStatus().isNotFound();
    verify(restaurantService).getRestaurantWithMenu(anyLong());
  }
}