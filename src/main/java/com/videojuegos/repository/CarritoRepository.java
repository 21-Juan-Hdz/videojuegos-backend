package com.videojuegos.repository;

import com.videojuegos.model.Carrito;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.videojuegos.dto.CarritoSchemas;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    @Query(value = "SELECT c.*, p.imagen_url, p.titulo FROM carrito c"
            + " INNER JOIN producto p ON c.id_videojuego = p.id_videojuegos WHERE id_usuario = :id_usuario", nativeQuery = true)
    List<CarritoSchemas.CarritoByUser> findByUsuarioId(@Param("id_usuario") Long id_usuario);
    
    @Query(value="SELECT * FROM carrito WHERE id_usuario = :id_usuario AND id_videojuego = :id_videojuego", nativeQuery = true)
    Optional<Carrito> findByIdUsuarioAndIdVideojuego(@Param ("id_usuario") Long id_usuario, @Param ("id_videojuego") Long id_videojuego);
}
