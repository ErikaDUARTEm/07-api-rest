package com.management.restaurant.models.order;

import com.management.restaurant.models.restaurant.Dish;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private Integer quantity;
  private Double price;
  private Long restaurantId;
  private Long menuId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "orden_id", nullable = false)
  private Orden orden;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "dish_id", nullable = false)
  private Dish dish;

  public Item(Long id, String name, Double price, Integer quantity, Orden orden) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.orden = orden;

  }
}
