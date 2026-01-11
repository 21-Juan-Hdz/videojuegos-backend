package com.videojuegos.service;

import com.videojuegos.model.Factura;
import com.videojuegos.repository.FacturaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {
  private final FacturaRepository repo;

  public FacturaService(FacturaRepository repo) {
    this.repo = repo;
  }

  public List<Factura> findAll() {
    return repo.findAll();
  }

  public Optional<Factura> findById(Long id) {
    return repo.findById(id);
  }

  public Factura save(Factura e) {
    return repo.save(e);
  }

  public void deleteById(Long id) {
    repo.deleteById(id);
  }
}
