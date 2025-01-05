package com.management.restaurant.services;

import com.management.restaurant.DTO.restaurant.MenuResponseDTO;
import com.management.restaurant.DTO.restaurant.MenuResquetDTO;
import com.management.restaurant.models.client.Client;
import com.management.restaurant.models.restaurant.Dish;
import com.management.restaurant.models.restaurant.MenuRestaurant;
import com.management.restaurant.repositories.DishRepository;
import com.management.restaurant.repositories.MenuRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class MenuService {

  private MenuRepository menuRepository;
  private DishRepository dishRepository;

  @Autowired
  public MenuService(MenuRepository menuRepository, DishRepository dishRepository) {
    this.menuRepository = menuRepository;
    this.dishRepository = dishRepository;
  }


  public MenuRestaurant createMenu(MenuRestaurant menuRestaurant) {
    return menuRepository.save(menuRestaurant);
  }
  public List<MenuRestaurant> getAllMenu(){
    return menuRepository.findAll();
  }
  public MenuRestaurant findMenuByRestaurantId(Long restaurantId) {
    return menuRepository.findByRestaurantId(restaurantId)
      .orElseThrow(() -> new RuntimeException("Menú no encontrado para el restaurante con ID: " + restaurantId));
  }

  public MenuRestaurant updateMenu(Long restaurantId, String description, List<Dish> dishes) {
    MenuRestaurant menu = findMenuByRestaurantId(restaurantId);
    menu.setDescription(description);
    for (Dish dish : dishes) {
      dish.setMenuRestaurant(menu);
    }
    dishRepository.saveAll(dishes);
    return menuRepository.save(menu);
  }
}
