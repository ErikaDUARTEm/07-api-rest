package com.management.restaurant.services;

import com.management.restaurant.DTO.restaurant.MenuResquetDTO;
import com.management.restaurant.models.restaurant.MenuRestaurant;
import com.management.restaurant.repositories.MenuRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class MenuService {
  private MenuRepository repository;

  @Autowired
  public MenuService(MenuRepository repository) {
    this.repository = repository;
  }

  public MenuRestaurant createMenu(MenuRestaurant menuRestaurant) {
    return repository.save(menuRestaurant);
  }
  public List<MenuRestaurant> getAllMenu(){
    return repository.findAll();
  }
}
