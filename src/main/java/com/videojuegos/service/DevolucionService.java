package com.videojuegos.service;

import com.videojuegos.model.Devolucion;
import com.videojuegos.repository.DevolucionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DevolucionService {
  private final DevolucionRepository repo;

  public DevolucionService(DevolucionRepository repo) {
    this.repo = repo;
  }

  public List<Devolucion> findAll() {
    return repo.findAll();
  }

  public Optional<Devolucion> findById(Long id) {
    return repo.findById(id);
  }

  public Devolucion save(Devolucion e) {
    return repo.save(e);
  }

  public void deleteById(Long id) {
    repo.deleteById(id);
  }
  
  public Long findIdVenta(Long id_factura){
      return repo.findIdVenta(id_factura);
  }
  public List<Devolucion> findByIdUsuario(Long id_usuario){
      return repo.findByIdUsuario(id_usuario);
  }
}
