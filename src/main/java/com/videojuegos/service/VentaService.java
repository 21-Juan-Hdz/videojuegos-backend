package com.videojuegos.service;

import com.videojuegos.model.Venta;
import com.videojuegos.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaService {
  private final VentaRepository repo;

  public VentaService(VentaRepository repo) {
    this.repo = repo;
  }

  public List<Venta> findAll() {
    return repo.findAll();
  }

  public Optional<Venta> findById(Long id) {
    return repo.findById(id);
  }

  public Venta save(Venta e) {
    return repo.save(e);
  }

  public void deleteById(Long id) {
    repo.deleteById(id);
  }
}
