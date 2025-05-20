package com.management.restaurant.services;

import com.management.restaurant.DTO.restaurant.MenuResquetDTO;
import com.management.restaurant.models.restaurant.Dish;
import com.management.restaurant.models.restaurant.MenuRestaurant;
import com.management.restaurant.models.restaurant.Restaurant;
import com.management.restaurant.repositories.DishRepository;
import com.management.restaurant.repositories.ItemRepository;
import com.management.restaurant.repositories.MenuRepository;
import com.management.restaurant.repositories.RestaurantRepository;
import com.management.restaurant.utils.MenuDtoConverter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class MenuService {

  private MenuRepository menuRepository;
  private DishRepository dishRepository;
  private RestaurantRepository restaurantRepository;
  private ItemRepository itemRepository;

  @Autowired
  public MenuService(MenuRepository menuRepository, DishRepository dishRepository, RestaurantRepository restaurantRepository, ItemRepository itemRepository) {
    this.menuRepository = menuRepository;
    this.dishRepository = dishRepository;
    this.restaurantRepository = restaurantRepository;
    this.itemRepository = itemRepository;
  }

  public MenuRestaurant addMenu(MenuResquetDTO menuRequestDTO) {
    Restaurant restaurant = restaurantRepository.findById(menuRequestDTO.getRestaurantId())
      .orElseThrow(() -> new IllegalArgumentException("Restaurante no encontrado"));
    Optional<MenuRestaurant> existingMenu = menuRepository.findByRestaurant_Id(restaurant.getId());
    if (existingMenu.isPresent()) {
      throw new IllegalArgumentException("El restaurante ya tiene un menu asociado.");
    }
    MenuRestaurant menu = MenuDtoConverter.convertToEntity(menuRequestDTO, restaurant);
    return menuRepository.save(menu);
  }

  public MenuRestaurant findMenuByRestaurantId(Long restaurantId) {
    return menuRepository.findByRestaurant_Id(restaurantId)
      .orElseThrow(() -> new RuntimeException("Menu no encontrado para el restaurante con ID: " + restaurantId));
  }

  @Transactional
  public MenuRestaurant updateMenu(MenuResquetDTO menuResquetDTO) {
    Restaurant restaurant = restaurantRepository.findById(menuResquetDTO.getRestaurantId())
      .orElseThrow(() -> new IllegalArgumentException("Restaurante no encontrado"));

    MenuRestaurant menu = findMenuByRestaurantId(restaurant.getId());
    menu.setDescription(menuResquetDTO.getDescription());

    List<Dish> dishes = menuResquetDTO.getDishes().stream()
      .map(dishDTO -> {
        Dish dish = new Dish();
        dish.setName(dishDTO.getName());
        dish.setPrice(dishDTO.getPrice());
        dish.setPopular(dishDTO.getPopular());
        dish.setMenuRestaurant(menu);
        return dish;
      }).collect(Collectors.toList());

    dishRepository.saveAll(dishes);
    return menuRepository.save(menu);
  }

  @Transactional
  public void deleteMenu(Long restaurantId) {
    Optional<MenuRestaurant> optionalMenu = menuRepository.findByRestaurant_Id(restaurantId);
    if (optionalMenu.isPresent()) {
      MenuRestaurant menu = optionalMenu.get();
      menu.getDishes().forEach(dish -> {
        itemRepository.deleteByDishId(dish.getId());
      });
      dishRepository.deleteAll(menu.getDishes());
      menuRepository.delete(menu);
    } else {
      throw new EntityNotFoundException("Menu no encontrado con id restaurante: " + restaurantId);
    }
  }
}


