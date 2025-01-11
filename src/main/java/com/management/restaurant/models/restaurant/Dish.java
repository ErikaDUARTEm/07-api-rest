package com.management.restaurant.models.restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FetchType;
import jakarta.persistence.FieldResult;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SqlResultSetMapping;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SqlResultSetMapping(
  name = "DishMapping",
  entities = { @EntityResult( entityClass = Dish.class, fields = {
    @FieldResult(name = "id", column = "dish_id"),
    @FieldResult(name = "name", column = "dish_name"),
    @FieldResult(name = "price", column = "dish_price") })})
public class Dish {
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;
  @Column(name = "name")
  private String name;
  @Column(name = "price")
  private Double price;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "menu_restaurant_id", referencedColumnName = "id_menu")
  private MenuRestaurant menuRestaurant;

  private Boolean popular = false;

  public Dish(Long id, String name, MenuRestaurant menuRestaurant, Double price, Boolean popular) {
    this.id = id;
    this.name = name;
    this.menuRestaurant = menuRestaurant;
    this.price = price;
    this.popular = popular;
  }

  public Dish(String name, Double price, Boolean popular, MenuRestaurant menuRestaurant) {
    this.name = name;
    this.price = price;
    this.popular = popular;
    this.menuRestaurant = menuRestaurant;
  }
}
