package com.rancho.gestorGranja.service;

import com.rancho.gestorGranja.entity.Animal;
import java.util.List;

public interface AnimalService extends BaseService<Animal, Long> {
    // Métodos específicos de Animal
    List<Animal> buscarAnimales(String nombre, Animal.EspeciePredefinida especie, 
                              Integer saludMinima, Integer saludMaxima, Long ubicacionId);
    List<Animal> findBySaludLessThan(int salud);
    List<Animal> findByEspecie(Animal.EspeciePredefinida especie);
    List<Animal> findByUbicacionId(Long ubicacionId);
}
