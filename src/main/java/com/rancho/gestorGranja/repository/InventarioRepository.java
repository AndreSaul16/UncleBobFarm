package com.rancho.gestorGranja.repository;

import com.rancho.gestorGranja.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    boolean existsByNombre(String nombre);
    List<Inventario> findByTipo(Inventario.TipoItem tipo);
    
    @Query("SELECT i FROM Inventario i WHERE i.cantidad <= i.cantidadMinima")
    List<Inventario> findNecesitanReposicion();
} 