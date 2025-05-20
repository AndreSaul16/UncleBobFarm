package com.rancho.gestorGranja.service.impl;

import com.rancho.gestorGranja.entity.*;
import com.rancho.gestorGranja.repository.ActividadRepository;
import com.rancho.gestorGranja.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ActividadServiceImpl implements BaseService<Actividad, Long> {
    
    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private InventarioServiceImpl inventarioService;

    @Autowired
    private AnimalServiceImpl animalService;

    @Override
    public List<Actividad> findAll() {
        return actividadRepository.findAll();
    }

    @Override
    public Optional<Actividad> findById(Long id) {
        return actividadRepository.findById(id);
    }

    @Override
    public Actividad save(Actividad actividad) {
        // Validar que la fecha no sea futura
        if (actividad.getFecha().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("La fecha de la actividad no puede ser futura");
        }

        // Validar que el tipo de actividad requiere items
        if (actividad.getTipo().getTipoItemRequerido() != null) {
            // Buscar un item del tipo requerido en el inventario
            List<Inventario> itemsDisponibles = inventarioService.findByTipo(actividad.getTipo().getTipoItemRequerido());
            if (itemsDisponibles.isEmpty()) {
                throw new RuntimeException("No hay " + actividad.getTipo().getTipoItemRequerido() + 
                    " disponible en el inventario para realizar esta actividad");
            }

            // Usar el primer item disponible
            Inventario item = itemsDisponibles.get(0);
            actividad.setItemsUtilizados(List.of(item));
            actividad.setCantidadUtilizada(actividad.getTipo().getCantidadRequerida());

            // Actualizar el inventario
            inventarioService.actualizarCantidad(item.getId(), -actividad.getCantidadUtilizada());
        }

        // Aplicar efectos a los animales
        if (actividad.getAfectaA() != null && !actividad.getAfectaA().isEmpty()) {
            for (Animal animal : actividad.getAfectaA()) {
                int nuevaSalud = Math.min(100, animal.getSalud() + actividad.getTipo().getEfectoSalud());
                animal.setSalud(nuevaSalud);
                animalService.save(animal);
            }
        }

        return actividadRepository.save(actividad);
    }

    @Override
    public void deleteById(Long id) {
        actividadRepository.deleteById(id);
    }

    public List<Actividad> findByEmpleado(Empleado empleado) {
        return actividadRepository.findByRealizadaPor(empleado);
    }

    public List<Actividad> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin) {
        return actividadRepository.findByFechaBetween(inicio, fin);
    }

    public List<Actividad> findByTipo(Actividad.TipoActividad tipo) {
        return actividadRepository.findByTipo(tipo);
    }

    public List<Actividad> findByAnimal(Animal animal) {
        return actividadRepository.findByAfectaAContaining(animal);
    }

    public List<Actividad.TipoActividad> getTiposActividadDisponibles() {
        return List.of(Actividad.TipoActividad.values());
    }
} 