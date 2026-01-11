/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.videojuegos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

public final class FavoritoSchemas {
    private FavoritoSchemas() {}
    public interface FavoritosByUser{
        Long getIdFavorito();
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
    }
    
    @Data
    public static class FavoritoCreate{
        @JsonProperty("id_usuario")
        private Long idUsuario;
        @JsonProperty("id_videojuego")
        private Long idVideojuego;
    }
}
