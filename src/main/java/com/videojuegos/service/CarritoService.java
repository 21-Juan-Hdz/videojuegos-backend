package com.videojuegos.service;

import com.videojuegos.model.Carrito;
import com.videojuegos.repository.CarritoRepository;
import org.springframework.stereotype.Service;
import com.videojuegos.dto.CarritoSchemas;

import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {
  private final CarritoRepository repo;

  public CarritoService(CarritoRepository repo) {
    this.repo = repo;
  }

  public List<Carrito> findAll() {
    return repo.findAll();
  }

  public Optional<Carrito> findById(Long id) {
    return repo.findById(id);
  }
  
  public List<CarritoSchemas.CarritoByUser> findByUsuarioId(Long id_usuario){
      return repo.findByUsuarioId(id_usuario);
  }

  public Carrito save(Carrito e) {
    return repo.save(e);
  }

  public void deleteById(Long id) {
    repo.deleteById(id);
  }
  public Optional<Carrito> findByIdUsuarioAndIdVideojuego(Long id_usuario, Long id_videojuego){
      return repo.findByIdUsuarioAndIdVideojuego(id_usuario, id_videojuego);
  }
}
