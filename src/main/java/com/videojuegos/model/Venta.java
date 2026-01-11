package com.videojuegos.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_venta")
  private Long idVenta;

  @JoinColumn(name = "id_usuario")
  private Long idUsuario;

  @Column(name = "fecha_venta")
  private LocalDateTime fechaVenta;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal total;

  @Column(name = "metodo_pago", length = 50)
  private String metodoPago;

  @Column(length = 30)
  private String estado;
}
