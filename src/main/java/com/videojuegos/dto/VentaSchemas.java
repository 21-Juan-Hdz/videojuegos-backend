package com.videojuegos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

public class VentaSchemas {

    private VentaSchemas() {
    }

    @Data
    public static class VentaDetalleRequest {

        @Schema(nullable = false)
        @JsonProperty("id_usuario")
        private Long idUsuario;
        @Schema(nullable = false)
        @JsonProperty("id_videojuego")
        private Long idVideojuego;
        @Schema(nullable = false)
        private Integer cantidad;
        @Schema(nullable = false)
        @JsonProperty("precio_unitario")
        private BigDecimal precioUnitario;
        @Schema(nullable = false)
        private BigDecimal subtotal;
    }
    @Data
    public static class VentaCreate {

        @Schema(nullable = false)
        @JsonProperty("id_usuario")
        private Long idUsuario;
        @Schema(nullable = false)
        private BigDecimal total;
        @Schema(nullable = false)
        @JsonProperty("metodo_pago")
        private String metodoPago;
        @Schema(description = "Listado de videojuegos que se venderan.", nullable = false)
        @JsonProperty("detalle")
        private List<VentaDetalleRequest> detalle;
    }
    public interface VentaDetalleResponse{
        Long getIdDetalleVenta();
        Long getIdVenta();
        Long getIdVideojuego();
        Integer getCantidad();
        BigDecimal getPrecioUnitario();
        BigDecimal getSubtotal();   
    }
    
    public interface VentaResponse{
        Long getIdVenta();
        Long getIdUsuario();
        LocalDateTime getFechaVenta();
        BigDecimal getTotal();
        String getMetodoPago();
        String getEstado();
    }
}
