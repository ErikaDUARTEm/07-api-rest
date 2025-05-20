package com.management.restaurant.DTO.restaurant;

import io.swagger.v3.oas.annotations.media.Schema;
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
  @Schema(type = "string", format = "time", pattern = "HH:mm",
    description = "Horario de apertura en formato HH:mm", example = "11:00")
  private LocalTime openingHours;
  @NotNull
  @Schema(type = "string", format = "time", pattern = "HH:mm",
    description = "Horario de cierre en formato HH:mm", example = "21:00")
  private LocalTime closingHours;


}
