package com.videojuegos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "carrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carrito {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_carrito")
  private Long idCarrito;
  @JoinColumn(name = "id_usuario")
  private Long idUsuario;
  @JoinColumn(name = "id_videojuego")
  private Long idVideojuego;
  @Column(name = "fecha_creacion")
  private LocalDateTime fechaCreacion;
  @Column(  nullable = false)
  private Integer cantidad;
  @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
  private BigDecimal precioUnitario;
}
