package com.videojuegos.repository;

import com.videojuegos.model.Venta;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM carrito WHERE id_usuario = :id_usuario AND id_videojuego = :id_videojuego;", nativeQuery = true)
    void DeleteCarrito(@Param("id_usuario")Long id_usuario, @Param("id_videojuego")Long id_videojuego);
}
