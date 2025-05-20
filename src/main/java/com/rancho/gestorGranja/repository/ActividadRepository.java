package com.rancho.gestorGranja.repository;

import com.rancho.gestorGranja.entity.Actividad;
import com.rancho.gestorGranja.entity.Animal;
import com.rancho.gestorGranja.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    List<Actividad> findByRealizadaPor(Empleado empleado);
    List<Actividad> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Actividad> findByTipo(Actividad.TipoActividad tipo);
    List<Actividad> findByAfectaAContaining(Animal animal);
} 