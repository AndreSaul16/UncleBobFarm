package com.rancho.gestorGranja.repository;

import com.rancho.gestorGranja.entity.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
    List<Ubicacion> findByTipo(Ubicacion.TipoUbicacion tipo);
    boolean existsByNombre(String nombre);
} 