package com.management.restaurant.services;

import com.management.restaurant.DTO.restaurant.DishRequestDTO;
import com.management.restaurant.models.restaurant.Dish;
import com.management.restaurant.models.restaurant.MenuRestaurant;
import com.management.restaurant.repositories.DishRepository;
import com.management.restaurant.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishService {
  private DishRepository repository;
  private MenuRepository menuRepository;

  @Autowired
  public DishService(DishRepository repository, MenuRepository menuRepository) {
    this.repository = repository;
    this.menuRepository = menuRepository;
  }
  public List<Dish> getAllDish(){
      return repository.findAllDishes();
  }
  public Dish createDish(DishRequestDTO dishRequestDTO) {
    Dish newDish = new Dish();
    newDish.setName(dishRequestDTO.getName());
    newDish.setPrice(dishRequestDTO.getPrice());
    newDish.setPopular(dishRequestDTO.getPopular() != null ? dishRequestDTO.getPopular() : false);

    // Buscar o crear el MenuRestaurant asociado
    MenuRestaurant menuRestaurant = menuRepository.findById(dishRequestDTO.getMenuRestaurantId())
      .orElseThrow(() -> new RuntimeException("MenuRestaurant not found"));

    newDish.setMenuRestaurant(menuRestaurant);

    return repository.save(newDish);
  }

  public Dish updateDish(Long id, DishRequestDTO dishRequest) {
    Dish existingDish = repository.findById(id)
      .orElseThrow(() -> new RuntimeException("Dish not found"));

    existingDish.setName(dishRequest.getName());
    existingDish.setPrice(dishRequest.getPrice());
    existingDish.setPopular(dishRequest.getPopular() != null ? dishRequest.getPopular() : false);

    if (dishRequest.getMenuRestaurantId() != null) {
      MenuRestaurant menuRestaurant = menuRepository.findById(dishRequest.getMenuRestaurantId())
        .orElseThrow(() -> new IllegalArgumentException("El menú asociado no existe."));
      existingDish.setMenuRestaurant(menuRestaurant);
    }

    return repository.save(existingDish);
  }

  public void deleteDish(Long id) {
    if (!repository.existsById(id)) {
      throw new IllegalArgumentException("El plato no existe.");
    }
    repository.deleteById(id);
  }
}
