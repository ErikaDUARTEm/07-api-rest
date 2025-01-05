package com.management.restaurant.models.restaurant;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Dish {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private Double price;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "menu_restaurant_id")
  private MenuRestaurant menuRestaurant;

  private Boolean popular = false;

  public Dish(Long id, String name, MenuRestaurant menuRestaurant, Double price, Boolean popular) {
    this.id = id;
    this.name = name;
    this.menuRestaurant = menuRestaurant;
    this.price = price;
    this.popular = popular;
  }
}
