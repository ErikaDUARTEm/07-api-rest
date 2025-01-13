package com.management.restaurant.models.order;

import com.management.restaurant.enums.StatusOrden;
import com.management.restaurant.models.client.Client;
import com.management.restaurant.strategy.IStatusOrdenStrategy;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Orden {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id_orden")
  private Long ordenId;

  @Column(name = "price_total", nullable = false)
  private Double priceTotal;

  @Column(name = "date_order", nullable = false)
  private LocalDateTime dateOrder;

  @Enumerated(EnumType.STRING)
  @Column(name = "status_order", nullable = false)
  private StatusOrden statusOrder;

  @ManyToOne
  @JoinColumn(name = "client_id", nullable = false)
  private Client client;

  @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Item> items = new ArrayList<>();

  @Transient
  private IStatusOrdenStrategy statusOrdenStrategy;

  public Orden(Long ordenId, Double priceTotal, LocalDateTime dateOrder, StatusOrden statusOrder, Client client, List<Item> items) {
    this.ordenId = ordenId;
    this.priceTotal = priceTotal;
    this.dateOrder = dateOrder;
    this.statusOrder = statusOrder;
    this.client = client;
    this.items = items;
    this.statusOrdenStrategy = null;
  }
  public void setItems(List<Item> items) {
    if (this.items != null) {
      this.items.forEach(item -> item.setOrden(null));
     }
    if (items != null) {
      items.forEach(item -> item.setOrden(this));
      this.items = new ArrayList<>(items);
    }else { this.items = new ArrayList<>();
    }
  }

  public void handleStatus() {
    if (this.statusOrdenStrategy != null)
    { this.statusOrdenStrategy.handle(this);
    }
  }
}
