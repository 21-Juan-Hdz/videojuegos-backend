package com.videojuegos.service;

import com.videojuegos.model.Favoritos;
import com.videojuegos.repository.FavoritoRepository;
import org.springframework.stereotype.Service;
import com.videojuegos.dto.FavoritoSchemas;

import java.util.List;
import java.util.Optional;

@Service
public class FavoritoService {
    private final FavoritoRepository repo;
    
    public FavoritoService (FavoritoRepository repo){
        this.repo = repo;
    }
    
    public List<Favoritos> findAll(){
        return repo.findAll();
    }
    public Favoritos save( Favoritos e){
        return repo.save(e);
    }
    
    public void deleteById(Long id){
        repo.deleteById(id);
    }
   public List<FavoritoSchemas.FavoritosByUser> findByIdUsuario(Long id_usuario){
       return repo.findByIdUsuario(id_usuario);
   }
}
