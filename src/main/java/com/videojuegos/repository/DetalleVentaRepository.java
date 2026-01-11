package com.videojuegos.repository;

import com.videojuegos.model.DetalleVenta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    @Query(value = "SELECT * FROM detalle_venta WHERE id_venta = :id_venta", nativeQuery = true)
    List<DetalleVenta> findByIdVenta(@Param("id_venta") Long id_venta);
}
