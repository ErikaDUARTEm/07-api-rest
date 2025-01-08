package com.management.restaurant.controllers;

import com.management.restaurant.DTO.restaurant.MenuResponseDTO;
import com.management.restaurant.DTO.restaurant.MenuResquetDTO;
import com.management.restaurant.models.restaurant.MenuRestaurant;
import com.management.restaurant.services.MenuService;
import com.management.restaurant.utils.MenuDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


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
