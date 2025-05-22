package com.management.restaurant.controllers;


import com.management.restaurant.DTO.ordens.DishDTO;
import com.management.restaurant.DTO.restaurant.DishPageResponseDTO;
import com.management.restaurant.DTO.restaurant.DishRequestDTO;
import com.management.restaurant.DTO.restaurant.DishResponseDTO;
import com.management.restaurant.models.restaurant.Dish;
import com.management.restaurant.services.DishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DishControllerTest {

  private final WebTestClient webTestClient;
  private DishService dishService;
  private Dish existingDish;
  private Dish updatedDish;
  private DishDTO dishDTO;

  public DishControllerTest(){
    dishService = mock(DishService.class);
    webTestClient = WebTestClient.bindToController(new DishController(dishService)).build();
  }

  @BeforeEach
  void setup() {
    existingDish = new Dish(1L, "Pasta", null, 12.99, false);
    updatedDish = new Dish(1L, "Pasta Updated", null, 15.99, true);
    dishDTO = new DishDTO();
    dishDTO.setName("Pasta Updated");
    dishDTO.setPrice(15.99);
    dishDTO.setPopular(true);

  }

  @Test
  @DisplayName("Obtener todos los platos paginados")
  void getAllDishesPaged() {
    Pageable pageable = PageRequest.of(0, 5);

    DishResponseDTO dish1 = new DishResponseDTO();
    dish1.setId(1L);
    dish1.setName("Pasta");
    dish1.setPrice(12.99);
    dish1.setPopular(false);

    DishResponseDTO dish2 = new DishResponseDTO();
    dish2.setId(2L);
    dish2.setName("Pizza");
    dish2.setPrice(15.99);
    dish2.setPopular(true);

    List<DishResponseDTO> dishList = List.of(dish1, dish2);
    DishPageResponseDTO pageResponse = new DishPageResponseDTO(dishList, 0, 5, 2, 1); // ✅ Usa DishPageResponseDTO

    when(dishService.getAllDish(pageable)).thenReturn(new PageImpl<>(dishList, pageable, dishList.size())); // ✅ Simula paginación

    webTestClient.get()
      .uri("/api/dish?page=0&size=5")
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody(DishPageResponseDTO.class)
      .value(response -> {
        assertEquals(2, response.content().size());
        assertEquals(0, response.pageNumber());
        assertEquals(5, response.pageSize());
        assertEquals(2, response.totalElements());
        assertEquals(1, response.totalPages());

        assertEquals(1L, response.content().get(0).getId());
        assertEquals("Pasta", response.content().get(0).getName());
        assertEquals(12.99, response.content().get(0).getPrice());
        assertFalse(response.content().get(0).getPopular());

        assertEquals(2L, response.content().get(1).getId());
        assertEquals("Pizza",response.content().get(1).getName());
        assertEquals(15.99, response.content().get(1).getPrice());
        assertTrue(response.content().get(1).getPopular());
      });

    Mockito.verify(dishService).getAllDish(pageable);
  }
  @Test
  @DisplayName("Crear plato nuevo")
  void createDish() {
    Dish dish = new Dish(1L, "Pasta", null, 12.99, false);

    when(dishService.createDish(any(DishRequestDTO.class))).thenReturn(dish);

    webTestClient
      .post()
      .uri("/api/dish")
      .bodyValue(dishDTO)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBodyList(DishDTO.class)
      .value(dishes -> {
        assertEquals(1L, dishes.get(0).getId());
        assertEquals("Pasta", dishes.get(0).getName());
        assertEquals(12.99, dishes.get(0).getPrice());
        assertFalse(dishes.get(0).getPopular());

      });
    Mockito.verify(dishService).createDish(any(DishRequestDTO.class));
  }

  @Test
  @DisplayName("Actualizar plato")
  void updateDish() {
    DishRequestDTO dishRequestDTO = new DishRequestDTO();
    dishRequestDTO.setName("Pasta");
    dishRequestDTO.setPrice(15.99);
    dishRequestDTO.setPopular(true);
    dishRequestDTO.setMenuRestaurantId(1L);

    when(dishService.updateDish(eq(existingDish.getId()), any(DishRequestDTO.class))).thenReturn(updatedDish);

    webTestClient.put()
        .uri("/api/dish/{id}", existingDish.getId())
        .bodyValue(dishRequestDTO)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(DishResponseDTO.class)
        .value(response -> {
          assertEquals(updatedDish.getId(), response.getId());
          assertEquals(updatedDish.getName(), response.getName());
          assertEquals(updatedDish.getPrice(), response.getPrice());
          assertTrue(updatedDish.getPopular());
        });
      Mockito.verify(dishService).updateDish(eq(existingDish.getId()), any(DishRequestDTO.class));
    }


  @Test
  @DisplayName("Eliminar plato")
  void deleteDish() {

    doNothing().when(dishService).deleteDish(existingDish.getId());

    webTestClient.delete()
      .uri("/api/dish/{id}", existingDish.getId())
      .exchange()
      .expectStatus().isNoContent();

    Mockito.verify(dishService).deleteDish(existingDish.getId());
  }

}