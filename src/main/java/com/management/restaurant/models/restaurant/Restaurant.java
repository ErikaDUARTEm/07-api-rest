package com.management.restaurant.models.restaurant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Restaurant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String address;

  @Column(name = "number_phone")
  private String phoneNumber;
  @Column(name = "opening_hours", columnDefinition = "TIME(0)")
  private LocalTime openingHours;
  @Column(name = "closing_hours", columnDefinition = "TIME(0)")
  private LocalTime closingHours;
  @OneToOne(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private MenuRestaurant menuRestaurant;

  public Restaurant(Long id, String name, String address, String phoneNumber, LocalTime openingHours, LocalTime closingHours, MenuRestaurant menuRestaurant) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.openingHours = openingHours;
    this.closingHours = closingHours;
    this.menuRestaurant = menuRestaurant;
  }
}
