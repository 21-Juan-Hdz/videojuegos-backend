package com.videojuegos.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleVenta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_detalle_venta")
  private Long idDetalleVenta;

  @JoinColumn(name = "id_venta")
  private Long idVenta;

  @JoinColumn(name = "id_videojuego")
  private Long idVideojuego;

  @Column(nullable = false)
  private Integer cantidad;

  @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
  private BigDecimal precioUnitario;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal subtotal;
}
