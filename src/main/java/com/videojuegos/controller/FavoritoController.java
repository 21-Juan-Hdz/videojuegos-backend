package com.videojuegos.controller;

import com.videojuegos.service.FavoritoService;
import com.videojuegos.model.Favoritos;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.videojuegos.dto.FavoritoSchemas;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {
    private final FavoritoService service;
    
    public FavoritoController(FavoritoService service){
        this.service = service;
    }
    
    @PostMapping
    public Favoritos create (@RequestBody FavoritoSchemas.FavoritoCreate body){
        Favoritos f = new Favoritos();
        f.setIdUsuario(body.getIdUsuario());
        f.setIdVideojuego(body.getIdVideojuego());
        
        return service.save(f);
    }
    
    @GetMapping
    public List<Favoritos> list(){
        return service.findAll();
    }
    
    @GetMapping("/{id_usuario}")
    public List<FavoritoSchemas.FavoritosByUser> ByUserId(@PathVariable Long id_usuario){
        return service.findByIdUsuario(id_usuario);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
