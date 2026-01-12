package com.videojuegos.controller;

import com.videojuegos.model.Producto;
import com.videojuegos.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.videojuegos.dto.ProductoSchemas;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/producto")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Producto> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> get(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/ByUserLogged/{id_usuario}")
    public ResponseEntity<List<ProductoSchemas.ProductosByUserLogged>> 
        getByUserLogged(@PathVariable Long id_usuario){
        List<ProductoSchemas.ProductosByUserLogged> p = service.findByuserLogged(id_usuario);
        return ResponseEntity.ok(p);
    }

    @PostMapping
    public Producto create(@RequestBody ProductoSchemas.ProductoCreate body) {
        Producto p = new Producto();
        p.setTitulo(body.getTitulo());
        p.setDescripcion(body.getDescripcion());
        p.setPrecio(body.getPrecio());
        p.setStock(body.getStock());
        p.setPlataforma(body.getPlataforma());
        p.setGenero(body.getGenero());
        p.setFechaLanzamiento(body.getFecha_lanzamiento());
        p.setEstado(body.getEstado());
        p.setImagenUrl(body.getImagen_url());

        return service.save(p);
    }

    /*@PutMapping("/{id}")
  public ResponseEntity<Producto> update(@PathVariable Long id, @RequestBody Producto body) {
    body.setIdVideojuegos(id);
    return ResponseEntity.ok(service.save(body));
  } */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(
            @PathVariable Long id,
            @RequestBody ProductoSchemas.ProductoUpdate body) {

        Optional<Producto> verify = service.findById(id);

        if (verify.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Producto p = verify.get();

        p.setTitulo(body.getTitulo());
        p.setDescripcion(body.getDescripcion());
        p.setPrecio(body.getPrecio());
        p.setStock(body.getStock());
        p.setPlataforma(body.getPlataforma());
        p.setGenero(body.getGenero());
        p.setFechaLanzamiento(body.getFecha_lanzamiento());
        p.setEstado(body.getEstado());
        p.setImagenUrl(body.getImagen_url());

        return ResponseEntity.ok(service.save(p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
}
