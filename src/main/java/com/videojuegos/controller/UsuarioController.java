package com.videojuegos.controller;

import com.videojuegos.model.Usuario;
import com.videojuegos.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
  private final UsuarioService service;

  public UsuarioController(UsuarioService service) {
    this.service = service;
  }

  @GetMapping
  public List<Usuario> list() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Usuario> get(@PathVariable Long id) {
    return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Usuario create(@RequestBody Usuario body) {
    return service.save(body);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody Usuario body) {
    body.setIdUsuario(id);
    return ResponseEntity.ok(service.save(body));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
