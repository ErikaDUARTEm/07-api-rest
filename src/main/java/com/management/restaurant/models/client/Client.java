package com.management.restaurant.models.client;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String email;

  @Column(name = "number_phone")
  private String numberPhone;

  @Column(name = "is_frecuent")
  private Boolean isFrecuent = false;

  public Client(String name, String email, String numberPhone) {
    this.name = name;
    this.email = email;
    this.numberPhone = numberPhone;
  }

  public Client(String name, String email, String numberPhone, Boolean isFrecuent) {
    this.name =name;
    this.email = email;
    this.numberPhone = numberPhone;
    this.isFrecuent = isFrecuent;
  }

  public Client(Long clientId) {
    this.id = clientId;
  }
}
