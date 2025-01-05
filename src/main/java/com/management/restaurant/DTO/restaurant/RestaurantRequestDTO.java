package com.management.restaurant.DTO.restaurant;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class RestaurantRequestDTO {
  @NotNull
  private String name;
  @NotNull
  private String address;
  @NotNull
  private String phoneNumber;
  @NotNull
  private LocalTime openingHours;
  @NotNull
  private LocalTime closingHours;


}
