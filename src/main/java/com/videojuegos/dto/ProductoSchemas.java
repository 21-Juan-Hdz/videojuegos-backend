/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.videojuegos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public final class ProductoSchemas {

    private ProductoSchemas() {
    }
    @Data
    public static class ProductoBase {
        private String titulo;
        private String descripcion;
        private BigDecimal precio;
        private Integer stock;
        private String plataforma;
        private String genero;
        @JsonProperty("fecha_lanzamiento")
        private LocalDate fecha_lanzamiento;
        private String estado;
        @JsonProperty("imagen_url")
        private String imagen_url;
    }
    @Data
    public static class ProductoCreate extends ProductoBase {}
    
    @Data
    public static class ProductoUpdate extends ProductoBase {}
    
    @Data
    public static class ProductoInDBBase extends ProductoBase{
        private Long idVideojuegos;
    }
    
    public interface ProductosByUserLogged {
        Long getIdVideojuegos();
        String getTitulo();
        String getDescripcion();
        BigDecimal getPrecio();
        Integer getStock();
        String getPlataforma();
        String getGenero();
        LocalDate getFechaLanzamiento();
        String getEstado();
        String getImagenUrl();
        Integer getFav();
    }

}
