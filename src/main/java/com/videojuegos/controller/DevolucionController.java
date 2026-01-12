package com.videojuegos.controller;

import com.videojuegos.model.Devolucion;
import com.videojuegos.service.DevolucionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import com.videojuegos.dto.DevolucionSchemas;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/devoluciones")
@CrossOrigin(origins = "*")
public class DevolucionController {

    private final DevolucionService service;
    private final TransactionTemplate tx;
    private final JdbcTemplate jdbc;

    public DevolucionController(DevolucionService service, PlatformTransactionManager transactionManager,
            JdbcTemplate jdbc) {
        this.service = service;
        this.tx = new TransactionTemplate(transactionManager);
        this.jdbc = jdbc;
    }

    @GetMapping
    public List<Devolucion> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Devolucion> get(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Devolucion create(@RequestBody DevolucionSchemas.DevolucionCreate body) {
        Devolucion de = new Devolucion();
        de.setIdUsuario(body.getIdUsuario());
        de.setIdFactura(body.getIdFactura());
        de.setFechaDevolucion(LocalDateTime.now());
        de.setDescripcion(body.getDescripcion());
        de.setEstado(body.getEstado());
        de.setObservacion(body.getObservacion());
        // BUSCAMOS EL ID DE LA VENTA
        Long idV = service.findIdVenta(body.getIdFactura());
        //ACTUALIZAMOS ESTATUS DE LA VENTA
        jdbc.update("""
                        UPDATE venta SET estado = CANCELADA WHERE id_venta = ?  """, idV);

        return service.save(de);
    }
    
    @GetMapping("/ByIdUsuario/{id}")
    public List<Devolucion> getByIdUsuario(@PathVariable Long id_usuario) {
        return service.findByIdUsuario(id_usuario);
    }

}
