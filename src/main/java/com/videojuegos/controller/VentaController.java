package com.videojuegos.controller;

import com.videojuegos.dto.VentaRequest;
import com.videojuegos.model.*;
import com.videojuegos.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/venta")
@CrossOrigin(origins = "*")
public class VentaController {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public VentaController(VentaRepository ventaRepository,
                           DetalleVentaRepository detalleVentaRepository,
                           UsuarioRepository usuarioRepository,
                           ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    @GetMapping("/ByIdUsuario/{idUsuario}")
    public ResponseEntity<List<Map<String, Object>>> getByUsuario(@PathVariable Long idUsuario) {
        // 1. Buscamos al usuario real primero
        Usuario u = usuarioRepository.findById(idUsuario).orElse(null);
        
        if (u == null) {
            return ResponseEntity.ok(List.of()); // Retorna lista vacía si no existe
        }

        // 2. Buscamos las ventas
        List<Venta> ventas = ventaRepository.findByUsuario(u);

        // 3. Convertimos a un mapa simple (Snake Case) para asegurar que el Frontend lo lea
        List<Map<String, Object>> respuesta = ventas.stream().map(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id_venta", v.getIdVenta());
            map.put("fecha_venta", v.getFechaVenta());
            map.put("total", v.getTotal());
            map.put("estado", v.getEstado());
            // No enviamos el usuario completo para evitar bucles infinitos
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // CREAR VENTA (COMPRA)
    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody VentaRequest req) {
        Usuario usuario = usuarioRepository.findById(req.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setTotal(req.getTotal());
        venta.setMetodoPago(req.getMetodoPago());
        venta.setEstado("COMPLETADA");
        venta.setFechaVenta(LocalDateTime.now());

        Venta ventaGuardada = ventaRepository.save(venta);

        for (VentaRequest.DetalleRequest det : req.getDetalles()) {
            Producto prod = productoRepository.findById(det.getIdVideojuego())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            DetalleVenta dv = new DetalleVenta();
            dv.setVenta(ventaGuardada);
            dv.setProducto(prod);
            dv.setCantidad(det.getCantidad());
            dv.setPrecioUnitario(det.getPrecioUnitario());
            dv.setSubtotal(det.getSubtotal());

            detalleVentaRepository.save(dv);
        }

        // Devolvemos un mapa simple para evitar error de serialización
        Map<String, Object> resp = new HashMap<>();
        resp.put("id_venta", ventaGuardada.getIdVenta());
        resp.put("estado", "OK");
        
        return ResponseEntity.ok(resp);
    }
}
