package com.videojuegos.service;

import com.videojuegos.dto.ProductoSchemas;
import com.videojuegos.model.Producto;
import com.videojuegos.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
  private final ProductoRepository repo;

  public ProductoService(ProductoRepository repo) {
    this.repo = repo;
  }

  public List<Producto> findAll() {
    return repo.findAll();
  }

  public Optional<Producto> findById(Long id) {
    return repo.findById(id);
  }

  public Producto save(Producto e) {
    return repo.save(e);
  }

  public void deleteById(Long id) {
    repo.deleteById(id);
  }
  
  public List<ProductoSchemas.ProductosByUserLogged> findByuserLogged(Long id_usuario){
      return repo.findByUserLogged(id_usuario);
  }
}
