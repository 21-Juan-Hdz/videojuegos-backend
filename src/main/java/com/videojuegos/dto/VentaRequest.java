package com.videojuegos.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class VentaRequest {

    @JsonProperty("id_usuario")
    private Long idUsuario;

    private BigDecimal total;

    @JsonProperty("metodo_pago")
    private String metodoPago;

    @JsonProperty("detalle")
    private List<DetalleRequest> detalles;

    @Data
    public static class DetalleRequest {
        @JsonProperty("id_videojuego")
        private Long idVideojuego;

        private Integer cantidad;

        @JsonProperty("precio_unitario")
        private BigDecimal precioUnitario;

        private BigDecimal subtotal;
    }
}