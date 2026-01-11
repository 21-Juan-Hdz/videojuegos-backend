/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.videojuegos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public final class CarritoSchemas {

    private CarritoSchemas() {}
    
    public static class CarritoBase{
        @Schema(nullable = false)
        @JsonProperty("id_usuario")
        private Long idUsuario;
        @JsonProperty("id_videojuego")
        private Long id_videojuego;
        private LocalDateTime fecha_creacion;
        private Integer cantidad;
        private BigDecimal precio_unitario;  
    }
    @Data
    public static class CarritoCreate {
        @Schema(nullable = true)
        @JsonProperty("id_usuario")
        private Long idUsuario;
        @JsonProperty("id_videojuego")
        private Long idVideojuego;
        private Integer cantidad;
        private BigDecimal precio_unitario;  
    }
    @Data
    public static class CarritoUpdate extends CarritoBase{}
    
    public interface CarritoByUser{
        Long getIdCarrito();
        Long getidUsuario();
        Long getIdVideojuego();
        String getTitulo();
        LocalDateTime getFechaCreacion();
        Integer getCantidad();
        BigDecimal getPrecioUnitario();
        String getImagenUrl();
    }
    
}
