package com.videojuegos.controller;

import com.videojuegos.model.Factura;
import com.videojuegos.service.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factura")
public class FacturaController {
  private final FacturaService service;

  public FacturaController(FacturaService service) {
    this.service = service;
  }

  @GetMapping
  public List<Factura> list() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Factura> get(@PathVariable Long id) {
    return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }


}
