package com.videojuegos.repository;

import com.videojuegos.model.Devolucion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DevolucionRepository extends JpaRepository<Devolucion, Long> {

    @Query(value = "select id_venta from factura WHERE id_factura = :id_factura;", nativeQuery = true)
    Long findIdVenta(Long id_factura);

    @Query(value = "SELECT d.* FROM devoluciones d "
            + "INNER JOIN factura f ON d.id_factura = f.id_factura "
            + "INNER JOIN venta v ON f.id_venta = v.id_venta "
            + "WHERE v.id_usuario = :id_usuario;", nativeQuery = true)
    List<Devolucion> findByIdUsuario(@Param("id_usuario") Long id_usuario);
}
