package com.management.restaurant.controllers;

import com.management.restaurant.DTO.restaurant.MenuResponseDTO;
import com.management.restaurant.DTO.restaurant.MenuResquetDTO;
import com.management.restaurant.models.restaurant.Dish;
import com.management.restaurant.models.restaurant.MenuRestaurant;
import com.management.restaurant.models.restaurant.Restaurant;
import com.management.restaurant.services.MenuService;
import com.management.restaurant.services.RestaurantService;
import com.management.restaurant.utils.DishDtoConverter;
import com.management.restaurant.utils.MenuDtoConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

  private MenuService service;

  @Autowired
  public MenuController(MenuService service) {
    this.service = service;

  }
  @PostMapping
  public MenuResponseDTO createMenu(@Validated @RequestBody MenuResquetDTO menuRequestDTO) {
    MenuRestaurant menu = service.addMenu(menuRequestDTO);
    return MenuDtoConverter.convertToResponseDTO(menu);
  }

  @GetMapping
  public List<MenuResponseDTO> allMenu() {
    List<MenuRestaurant> menus = service.getAllMenu();
    return menus.stream()
      .map(MenuDtoConverter::convertToResponseDTO)
      .collect(Collectors.toList());
  }
  @PutMapping()
  public MenuResponseDTO updateMenu(@Validated @RequestBody MenuResquetDTO menuRequestDTO) {
    MenuRestaurant updatedMenu = service.updateMenu(menuRequestDTO);
    return MenuDtoConverter.convertToResponseDTO(updatedMenu);
  }
  @DeleteMapping("/{restaurantId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteMenu(@PathVariable Long restaurantId) {
    service.deleteMenu(restaurantId);
  }
}
