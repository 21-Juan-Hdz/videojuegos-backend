package com.videojuegos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "devoluciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Devolucion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_devolucion")
  private Long idDevolucion;

  @JoinColumn(name = "id_usuario")
  private Long idUsuario;

 
  @JoinColumn(name = "id_factura")
  private Long idFactura;

  @Column(name = "fecha_devolucion")
  private LocalDateTime fechaDevolucion;

  @Column(columnDefinition = "TEXT")
  private String descripcion;

  @Column(length = 30)
  private String estado;

  @Column(columnDefinition = "TEXT")
  private String observacion;
}
