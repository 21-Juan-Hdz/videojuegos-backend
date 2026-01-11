package com.videojuegos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;



public class DevolucionSchemas {
    private DevolucionSchemas(){}
    
    @Data
    public static class DevolucionCreate{
        @JsonProperty("id_usuario")
        private Long idUsuario;
        @JsonProperty("id_factura")
        private Long idFactura;
        @JsonProperty("fecha_devolucion")
        LocalDateTime fechaDevolucion;
        private String descripcion;
        private String estado;
        private String observacion;
    }
}
