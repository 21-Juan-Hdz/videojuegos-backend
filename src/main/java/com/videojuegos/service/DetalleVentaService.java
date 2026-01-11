package com.videojuegos.service;

import com.videojuegos.model.DetalleVenta;
import com.videojuegos.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleVentaService {
  private final DetalleVentaRepository repo;

  public DetalleVentaService(DetalleVentaRepository repo) {
    this.repo = repo;
  }

  public List<DetalleVenta> findAll() {
    return repo.findAll();
  }

  public Optional<DetalleVenta> findById(Long id) {
    return repo.findById(id);
  }

  public DetalleVenta save(DetalleVenta e) {
    return repo.save(e);
  }

  public void deleteById(Long id) {
    repo.deleteById(id);
  }
  
  public List<DetalleVenta> findByIdVenta(Long id){
      return repo.findByIdVenta(id);
  }
}
