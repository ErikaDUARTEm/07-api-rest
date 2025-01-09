package com.management.restaurant.controllers;


import com.management.restaurant.DTO.ordens.OrdenRequestDTO;
import com.management.restaurant.DTO.ordens.OrdenResponseDTO;
import com.management.restaurant.enums.StatusOrden;
import com.management.restaurant.services.OrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {


  private final OrdenService ordenService;

  @Autowired
  public OrdenController(OrdenService ordenService) {
    this.ordenService = ordenService;
  }
  @PostMapping
  public ResponseEntity<OrdenResponseDTO> createOrden(@Validated @RequestBody OrdenRequestDTO ordenRequestDTO) {
    OrdenResponseDTO createdOrden = ordenService.createOrden(ordenRequestDTO);
    return ResponseEntity.ok(createdOrden);
  }

  @GetMapping
  public List<OrdenResponseDTO> getAllOrdenes() {
    return ordenService.getAllOrdenes();
  }

  @GetMapping("/{id}")
  public OrdenResponseDTO getOrdenById(@PathVariable Long id) {
    return ordenService.getOrdenById(id);
  }

  @PutMapping("/{id}")
  public OrdenResponseDTO updateOrden(@PathVariable Long id, @Validated @RequestBody OrdenRequestDTO ordenRequestDTO) {
    return ordenService.updateOrden(id, ordenRequestDTO);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteOrden(@PathVariable Long id) {
    ordenService.deleteOrden(id);
  }

  @PutMapping("/{id}/{newStatus}")
  public ResponseEntity<OrdenResponseDTO> changeStateOrder(@PathVariable Long id, @PathVariable String newStatus){
    StatusOrden status = StatusOrden.valueOf(newStatus.toUpperCase());
    ordenService.changeStateOrder(id, status);
    return ResponseEntity.ok(ordenService.getOrdenById(id));
  }
}
