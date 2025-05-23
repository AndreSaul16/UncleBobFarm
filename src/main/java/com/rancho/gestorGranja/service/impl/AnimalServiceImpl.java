package com.rancho.gestorGranja.service.impl;

import com.rancho.gestorGranja.entity.Animal;
import com.rancho.gestorGranja.repository.AnimalRepository;
import com.rancho.gestorGranja.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnimalServiceImpl extends BaseServiceImpl<Animal, Long, AnimalRepository> implements AnimalService {
    
    @Autowired
    public AnimalServiceImpl(AnimalRepository repository) {
        super(repository);
    }

    @Override
    public List<Animal> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Animal> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Animal save(Animal animal) {
        animal.validar(); // Validar antes de guardar
        return super.save(animal);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Animal> buscarAnimales(String nombre, Animal.EspeciePredefinida especie, 
                                     Integer saludMinima, Integer saludMaxima, Long ubicacionId) {
        return repository.findAll().stream()
            .filter(animal -> {
                // Filtrar por nombre (si se proporciona)
                if (nombre != null && !nombre.isEmpty()) {
                    if (!animal.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                        return false;
                    }
                }

                // Filtrar por especie (si se proporciona)
                if (especie != null && animal.getEspecie() != especie) {
                    return false;
                }

                // Filtrar por rango de salud (si se proporciona)
                if (saludMinima != null && animal.getSalud() < saludMinima) {
                    return false;
                }
                if (saludMaxima != null && animal.getSalud() > saludMaxima) {
                    return false;
                }

                // Filtrar por ubicación (si se proporciona)
                if (ubicacionId != null && 
                    (animal.getUbicacion() == null || 
                     !animal.getUbicacion().getId().equals(ubicacionId))) {
                    return false;
                }

                return true;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<Animal> findBySaludLessThan(int salud) {
        return repository.findBySaludLessThan(salud);
    }

    @Override
    public List<Animal> findByEspecie(Animal.EspeciePredefinida especie) {
        return repository.findByEspecie(especie);
    }

    @Override
    public List<Animal> findByUbicacionId(Long ubicacionId) {
        return repository.findByUbicacionId(ubicacionId);
    }
} 