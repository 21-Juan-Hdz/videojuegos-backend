package com.videojuegos.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.videojuegos.model.Venta;
import com.videojuegos.model.DetalleVenta;
import com.videojuegos.model.Factura;
import com.videojuegos.service.VentaService;
import com.videojuegos.service.DetalleVentaService;
import com.videojuegos.service.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.videojuegos.dto.VentaSchemas;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

import java.util.List;

@RestController
@RequestMapping("/api/venta")
public class VentaController {

    private final VentaService service;
    private final DetalleVentaService dvService;
    private final FacturaService fService;

    public VentaController(VentaService service, DetalleVentaService dvService, FacturaService fService) {
        this.service = service;
        this.dvService = dvService;
        this.fService = fService;
    }

    @GetMapping
    public List<Venta> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> get(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /*@PostMapping
  public Venta create(@RequestBody Venta body) {
    return service.save(body);
  }*/
    @PostMapping
    public ResponseEntity<VentaResponse> create(@RequestBody VentaSchemas.VentaCreate body) {
        try {
            Venta v = new Venta();
            v.setIdUsuario(body.getIdUsuario());
            v.setFechaVenta(LocalDateTime.now());
            v.setTotal(body.getTotal());
            v.setMetodoPago(body.getMetodoPago());
            v.setEstado("COMPLETADA");
            service.save(v);
            Long idVta = v.getIdVenta();
            // SE GUARDA DETALLE
            List<VentaSchemas.VentaDetalleRequest> detalle = body.getDetalle();
            detalle.forEach(det -> {
                DetalleVenta dv = new DetalleVenta();
                dv.setIdVenta(idVta);
                dv.setIdVideojuego(det.getIdVideojuego());
                dv.setCantidad(det.getCantidad());
                dv.setPrecioUnitario(det.getPrecioUnitario());
                dv.setSubtotal(det.getSubtotal());
                dvService.save(dv);
            });

            Factura fac = new Factura();
            fac.setIdVenta(idVta);
            fac.setNumeroFactura(UUID.randomUUID().toString());
            fac.setFechaEmision(LocalDateTime.now());
            BigDecimal total = body.getTotal();
            fac.setTotal(total);
            BigDecimal iva = total
                    .subtract(
                            total.divide(
                                    BigDecimal.valueOf(1.16),
                                    2, // escala (decimales)
                                    RoundingMode.HALF_UP
                            )
                    );
            fac.setImpuestos(iva);
            
            fService.save(fac);

            return ResponseEntity.status(200).body(new VentaResponse(idVta, "Venta realizada"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new VentaResponse(null, e.getMessage()));
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record VentaResponse(Long idVenta, String message) {

    }

    @GetMapping("/ByIdUsuario/{id}")
    public ResponseEntity<Venta> getByIdUsuario(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
