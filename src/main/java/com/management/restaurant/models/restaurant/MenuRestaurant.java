package com.management.restaurant.models.restaurant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MenuRestaurant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_menu")
  private Long idMenu;

  private String description;
  @OneToOne
  @JoinColumn(name = "restaurant_id",referencedColumnName = "id", unique = true)
  private Restaurant restaurant;

  @OneToMany(mappedBy = "menuRestaurant", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Dish> dishes = new ArrayList<>();;

  public MenuRestaurant(Long idMenu, String description, Restaurant restaurant, List<Dish> dishes) {
    this.idMenu = idMenu;
    this.description = description;
    this.restaurant = restaurant;
    this.dishes = dishes;
  }


}
