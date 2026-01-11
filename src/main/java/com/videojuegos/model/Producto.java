package com.videojuegos.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_videojuegos")
  private Long idVideojuegos;
  @Column(nullable = false, length = 200)
  private String titulo;
  @Column(columnDefinition = "TEXT")
  private String descripcion;
  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal precio;
  @Column(nullable = false)
  private Integer stock;
  @Column(length = 20)
  private String plataforma;
  @Column(length = 60)
  private String genero;
  @Column(name = "fecha_lanzamiento")
  private LocalDate fechaLanzamiento;
  @Column(length = 30)
  private String estado;
  @Column(columnDefinition = "LONGTEXT", name="imagen_url")
  private String imagenUrl;
}
