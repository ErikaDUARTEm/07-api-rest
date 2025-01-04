package com.management.restaurant.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRequestDTO {
  @NotNull(message = "El nombre no puede ser nulo")
  private String name;

  @NotNull(message = "El correo no puede ser nulo")
  @Email
  private String email;

  @NotNull(message = "El numero de teléfono no puede ser nulo")
  private String numberPhone;

}
