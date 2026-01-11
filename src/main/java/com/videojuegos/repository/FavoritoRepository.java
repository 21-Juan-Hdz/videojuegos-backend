package com.videojuegos.repository;

import com.videojuegos.model.Favoritos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.videojuegos.dto.FavoritoSchemas;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoritoRepository extends JpaRepository<Favoritos, Long> {

    @Query(value = "SELECT p.*, f.id_favorito FROM producto p "
            + "INNER JOIN favoritos f ON p.id_videojuegos = f.id_videojuego "
            + "WHERE f.id_usuario = :id_usuario;", nativeQuery = true)
    List<FavoritoSchemas.FavoritosByUser> findByIdUsuario(@Param("id_usuario") Long id_usuario);
}
