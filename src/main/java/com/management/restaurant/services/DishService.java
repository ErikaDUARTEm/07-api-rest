package com.management.restaurant.services;

import com.management.restaurant.DTO.restaurant.DishRequestDTO;
import com.management.restaurant.models.restaurant.Dish;
import com.management.restaurant.models.restaurant.MenuRestaurant;
import com.management.restaurant.repositories.DishRepository;
import com.management.restaurant.repositories.MenuRepository;
import com.management.restaurant.services.interfaces.IObserver;
import com.management.restaurant.services.observer.ObserverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService implements IObserver<Dish> {
  private final ObserverManager<Dish> observerManager;
  private final DishRepository repository;
  private final MenuRepository menuRepository;


  @Autowired
  public DishService(ObserverManager<Dish> observerManager, DishRepository repository, MenuRepository menuRepository) {
    this.observerManager = observerManager;
    this.repository = repository;
    this.menuRepository = menuRepository;
    addDishObserver();
  }
  public List<Dish> getAllDish(){
      return repository.findAllDishes();
  }
  public Dish createDish(DishRequestDTO dishRequestDTO) {
    Dish newDish = new Dish();
    newDish.setName(dishRequestDTO.getName());
    newDish.setPrice(dishRequestDTO.getPrice());
    newDish.setPopular(dishRequestDTO.getPopular() != null ? dishRequestDTO.getPopular() : false);
    MenuRestaurant menuRestaurant = menuRepository.findById(dishRequestDTO.getMenuRestaurantId())
      .orElseThrow(() -> new RuntimeException("MenuRestaurant not found"));

    newDish.setMenuRestaurant(menuRestaurant);
    return repository.save(newDish);
  }

  public Dish updateDish(Long id, DishRequestDTO dishRequest) {
    Dish existingDish = getExistingDish(id);
    updateDishDetails(existingDish, dishRequest);
    Dish updatedDish = repository.save(existingDish);
    notifyDishObservers(updatedDish);
    return updatedDish;
  }

  private Dish getExistingDish(Long id) {
    return repository.findById(id).orElseThrow(() -> new RuntimeException("Plato no encontrado."));
  }

  private void updateDishDetails(Dish dish, DishRequestDTO request) {
    dish.setName(request.getName());
    dish.setPrice(request.getPrice());
    dish.setPopular(request.getPopular() != null ? request.getPopular() : false);
    if (request.getMenuRestaurantId() != null) {
      MenuRestaurant menu = menuRepository.findById(request.getMenuRestaurantId())
        .orElseThrow(() -> new IllegalArgumentException("El menu asociado no existe."));
      dish.setMenuRestaurant(menu);
    }
  }

  public void deleteDish(Long id) {
    Dish dish = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("El plato no existe."));

    removeDishObserver(this);
    repository.deleteById(dish.getId());
  }

  public Dish findDishByName(String name) {
    return repository.findByName(name);
  }
  public void addDishObserver() {
    observerManager.addObserver(this);
  }

  public void removeDishObserver(IObserver<Dish> IObserver) {
    observerManager.removeObserver(IObserver);
  }

  public void notifyDishObservers(Dish dish) {
    observerManager.notifyObservers(dish);
  }

  @Override
  public void updateObserver(Dish dish) {
    Long purchaseCount = repository.countPopularDishes(dish.getId());
    if (purchaseCount > 100 && !dish.getPopular()) {
      dish.setPopular(true);
      dish.setPrice(calculateNewPriceWithIncrease(dish.getPrice()));
      repository.save(dish);
    }
  }
  private Double calculateNewPriceWithIncrease(Double currentPrice) {
    return currentPrice * 1.0573;
  }

}
