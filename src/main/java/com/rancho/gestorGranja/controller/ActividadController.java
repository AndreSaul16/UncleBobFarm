package com.rancho.gestorGranja.controller;

import com.rancho.gestorGranja.entity.Actividad;
import com.rancho.gestorGranja.entity.Animal;
import com.rancho.gestorGranja.entity.Empleado;
import com.rancho.gestorGranja.service.impl.ActividadServiceImpl;
import com.rancho.gestorGranja.service.impl.AnimalServiceImpl;
import com.rancho.gestorGranja.service.impl.EmpleadoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {

    @Autowired
    private ActividadServiceImpl actividadService;

    @Autowired
    private AnimalServiceImpl animalService;

    @Autowired
    private EmpleadoServiceImpl empleadoService;

    @GetMapping("/tipos")
    @SuppressWarnings("unchecked")
    public List<Map<String, ? extends Object>> getTiposActividad() {
        return actividadService.getTiposActividadDisponibles().stream()
            .map(tipo -> Map.of(
                "codigo", tipo.name(),
                "descripcion", tipo.getDescripcion(),
                "efectoSalud", tipo.getEfectoSalud(),
                "tipoItemRequerido", tipo.getTipoItemRequerido(),
                "cantidadRequerida", tipo.getCantidadRequerida()
            ))
            .collect(Collectors.toList());
    }

    @GetMapping
    public List<Actividad> getAllActividades() {
        return actividadService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actividad> getActividadById(@PathVariable Long id) {
        return actividadService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createActividad(@RequestBody Actividad actividad) {
        try {
            // Validar que el empleado existe
            if (actividad.getRealizadaPor() != null && actividad.getRealizadaPor().getId() != null) {
                var empleadoOpt = empleadoService.findById(actividad.getRealizadaPor().getId());
                if (empleadoOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body("Empleado no encontrado");
                }
                actividad.setRealizadaPor(empleadoOpt.get());
            }

            // Validar que los animales existen
            if (actividad.getAfectaA() != null && !actividad.getAfectaA().isEmpty()) {
                for (Animal animal : actividad.getAfectaA()) {
                    if (animal.getId() != null) {
                        var animalOpt = animalService.findById(animal.getId());
                        if (animalOpt.isEmpty()) {
                            return ResponseEntity.badRequest().body("Animal no encontrado: " + animal.getId());
                        }
                    }
                }
            }

            Actividad nuevaActividad = actividadService.save(actividad);
            return ResponseEntity.ok(nuevaActividad);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear actividad: " + e.getMessage());
        }
    }

    @GetMapping("/empleado/{empleadoId}")
    public ResponseEntity<?> getActividadesByEmpleado(@PathVariable Long empleadoId) {
        try {
            var empleadoOpt = empleadoService.findById(empleadoId);
            if (empleadoOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Empleado no encontrado");
            }
            List<Actividad> actividades = actividadService.findByEmpleado(empleadoOpt.get());
            return ResponseEntity.ok(actividades);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al buscar actividades: " + e.getMessage());
        }
    }

    @GetMapping("/animal/{animalId}")
    public ResponseEntity<?> getActividadesByAnimal(@PathVariable Long animalId) {
        try {
            var animalOpt = animalService.findById(animalId);
            if (animalOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Animal no encontrado");
            }
            List<Actividad> actividades = actividadService.findByAnimal(animalOpt.get());
            return ResponseEntity.ok(actividades);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al buscar actividades: " + e.getMessage());
        }
    }

    @GetMapping("/fecha")
    public ResponseEntity<?> getActividadesByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        try {
            List<Actividad> actividades = actividadService.findByFechaBetween(inicio, fin);
            return ResponseEntity.ok(actividades);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al buscar actividades: " + e.getMessage());
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> getActividadesByTipo(@PathVariable Actividad.TipoActividad tipo) {
        try {
            List<Actividad> actividades = actividadService.findByTipo(tipo);
            return ResponseEntity.ok(actividades);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al buscar actividades: " + e.getMessage());
        }
    }
} 