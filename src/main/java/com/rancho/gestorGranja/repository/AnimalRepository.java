package com.rancho.gestorGranja.repository;

import com.rancho.gestorGranja.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findByUbicacionId(Long ubicacionId);
    List<Animal> findBySaludLessThan(int salud);
    List<Animal> findByEspecie(Animal.EspeciePredefinida especie);
}
