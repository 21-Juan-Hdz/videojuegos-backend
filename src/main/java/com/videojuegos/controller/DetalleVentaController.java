package com.videojuegos.controller;

import com.videojuegos.model.DetalleVenta;
import com.videojuegos.service.DetalleVentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle-venta")
public class DetalleVentaController {

    private final DetalleVentaService service;

    public DetalleVentaController(DetalleVentaService service) {
        this.service = service;
    }

    @GetMapping
    public List<DetalleVenta> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVenta> get(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ByIdVenta/{id_venta}")
    public List<DetalleVenta> getByIdVenta(@PathVariable Long id_venta) {
        return service.findByIdVenta(id_venta);
    }
}
