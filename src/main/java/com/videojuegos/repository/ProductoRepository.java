package com.videojuegos.repository;

import com.videojuegos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.videojuegos.dto.ProductoSchemas;
import java.util.List;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query(value = "select p.*, if(f.id_favorito is not null AND f.id_usuario = :id_usuario , 1, 0)"
            + " fav  from producto p "
            + "LEFT JOIN favoritos f ON p.id_videojuegos = f.id_videojuego;", nativeQuery = true)
    List<ProductoSchemas.ProductosByUserLogged> findByUserLogged(@Param("id_usuario") Long id_usuario);
}
