package com.videojuegos.controller;

import com.videojuegos.model.Carrito;
import com.videojuegos.service.CarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.videojuegos.dto.CarritoSchemas;
import java.time.LocalDateTime;
        
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "*")
public class CarritoController {
  private final CarritoService service;

  public CarritoController(CarritoService service) {
    this.service = service;
  }
  @GetMapping
  public List<Carrito> list() {
    return service.findAll();
  }
  @GetMapping("/byUsuarioId/{id_usuario}")
  public ResponseEntity<List<CarritoSchemas.CarritoByUser>> getByUser(@PathVariable Long id_usuario) {
      List<CarritoSchemas.CarritoByUser> cart = service.findByUsuarioId(id_usuario);
      /*if(cart.isEmpty()){
          return ResponseEntity.notFound().build();
      }*/
    return ResponseEntity.ok(cart);
  }
  @PostMapping
  public Carrito create(@RequestBody CarritoSchemas.CarritoCreate body) {
      //BUSCAMOS QUE NO EXISTA EL PRODUCTO PARA EL USUARIO
      Optional<Carrito> c = service.findByIdUsuarioAndIdVideojuego(body.getIdUsuario(), body.getIdVideojuego());
      Carrito cart = new Carrito();
      if(c.isEmpty()){
          // SE AGREGA A LA BD
          cart.setIdUsuario(body.getIdUsuario());
          cart.setIdVideojuego(body.getIdVideojuego());
          cart.setFechaCreacion(LocalDateTime.now());
          cart.setCantidad(body.getCantidad());
          cart.setPrecioUnitario(body.getPrecio_unitario());
          
          return service.save(cart);
      }else{
          // SE ACTUALIZA EL REGISTRO
          cart = c.get();
          
          cart.setIdUsuario(body.getIdUsuario());
          cart.setIdVideojuego(body.getIdVideojuego());
          cart.setFechaCreacion(LocalDateTime.now());
          cart.setCantidad(body.getCantidad());
          cart.setPrecioUnitario(body.getPrecio_unitario());
          
          return service.save(cart);
      }
  }
  /*@PutMapping("/{id}")
  public ResponseEntity<Carrito> update(@PathVariable Long id, @RequestBody Carrito body) {
    body.setIdCarrito(id);
    return ResponseEntity.ok(service.save(body));
  }*/ 
 @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
