package com.management.restaurant.utils;


import com.management.restaurant.DTO.ordens.OrdenResponseDTO;
import com.management.restaurant.models.order.Orden;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class OrdenDtoConverterTest {

  @Test
  @DisplayName("Caso negativo, que orden sea null")
  void convertToResponseDTO() {
    Orden orden = null;
    OrdenResponseDTO ordenResponseDTO = OrdenDtoConverter.convertToResponseDTO(orden);
    assertNull(ordenResponseDTO, "El resultado debería ser nulo cuando orden es nulo.");
  }

}