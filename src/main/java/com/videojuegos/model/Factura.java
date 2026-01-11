package com.videojuegos.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "factura")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_factura")
  private Long idFactura;

  @JoinColumn(name = "id_venta")
  private Long idVenta;

  @Column(name = "numero_factura", nullable = false, unique = true, length = 50)
  private String numeroFactura;

  @Column(name = "fecha_emision")
  private LocalDateTime fechaEmision;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal total;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal impuestos;
}
