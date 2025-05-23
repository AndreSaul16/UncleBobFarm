package com.rancho.gestorGranja.controller;

import com.rancho.gestorGranja.dto.AnimalDTO;
import com.rancho.gestorGranja.entity.Animal;
import com.rancho.gestorGranja.entity.Ubicacion;
import com.rancho.gestorGranja.service.AnimalService;
import com.rancho.gestorGranja.service.impl.UbicacionServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/animales")
public class AnimalController {
    @Autowired
    private AnimalService animalService;

    @Autowired
    private UbicacionServiceImpl ubicacionService;

    @GetMapping("/especies")
    public List<Map<String, String>> getEspeciesDisponibles() {
        return Arrays.stream(Animal.EspeciePredefinida.values())
            .map(especie -> Map.of(
                "codigo", especie.name(),
                "nombreComun", especie.getNombreComun(),
                "nombreCientifico", especie.getNombreCientifico()
            ))
            .collect(Collectors.toList());
    }

    @GetMapping
    public List<AnimalDTO> getAllAnimales(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Animal.EspeciePredefinida especie,
            @RequestParam(required = false) Integer saludMinima,
            @RequestParam(required = false) Integer saludMaxima,
            @RequestParam(required = false) Long ubicacionId) {
        
        List<Animal> animales;
        // Si no hay filtros, devolver todos los animales
        if (nombre == null && especie == null && saludMinima == null && 
            saludMaxima == null && ubicacionId == null) {
            animales = animalService.findAll();
        } else {
            // Buscar por los filtros proporcionados
            animales = animalService.buscarAnimales(nombre, especie, saludMinima, saludMaxima, ubicacionId);
        }
        
        return animales.stream()
                .map(AnimalDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAnimalById(@PathVariable Long id) {
        return animalService.findById(id)
            .map(animal -> ResponseEntity.ok(new AnimalDTO(animal)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createAnimal(@Valid @RequestBody Animal animal) {
        try {
            // Si no se especifica ubicación, buscar o crear ubicación exterior
            if (animal.getUbicacion() == null) {
                var ubicacionExterior = ubicacionService.findByTipo(Ubicacion.TipoUbicacion.EXTERIOR)
                    .stream()
                    .findFirst()
                    .orElseGet(() -> {
                        Ubicacion nueva = new Ubicacion();
                        nueva.setNombre("Área Exterior");
                        nueva.setTipo(Ubicacion.TipoUbicacion.EXTERIOR);
                        return ubicacionService.save(nueva);
                    });
                animal.setUbicacion(ubicacionExterior);
            }

            Animal nuevoAnimal = animalService.save(animal);
            return ResponseEntity.ok(new AnimalDTO(nuevoAnimal));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear animal: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnimal(@PathVariable Long id, @Valid @RequestBody Animal animalDetails) {
        try {
            var animalOpt = animalService.findById(id);
            if (animalOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Animal animal = animalOpt.get();
            
            // Actualizar campos permitidos
            animal.setNombre(animalDetails.getNombre());
            animal.setEspecie(animalDetails.getEspecie());
            animal.setEdad(animalDetails.getEdad());
            animal.setSalud(animalDetails.getSalud());
            
            // Actualizar ubicación si se proporciona
            if (animalDetails.getUbicacion() != null) {
                animal.setUbicacion(animalDetails.getUbicacion());
            }

            Animal animalActualizado = animalService.save(animal);
            return ResponseEntity.ok(new AnimalDTO(animalActualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar animal: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnimal(@PathVariable Long id) {
        try {
            if (animalService.findById(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            animalService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar animal: " + e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarAnimales(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Animal.EspeciePredefinida especie,
            @RequestParam(required = false) Integer saludMinima,
            @RequestParam(required = false) Integer saludMaxima,
            @RequestParam(required = false) Long ubicacionId) {
        try {
            List<Animal> animales = animalService.buscarAnimales(nombre, especie, saludMinima, saludMaxima, ubicacionId);
            return ResponseEntity.ok(animales);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al buscar animales: " + e.getMessage());
        }
    }

    @GetMapping("/necesitan-atencion")
    public ResponseEntity<?> getAnimalesNecesitanAtencion() {
        try {
            List<Animal> animales = animalService.findBySaludLessThan(20);
            List<AnimalDTO> animalDTOs = animales.stream()
                .map(AnimalDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(animalDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al buscar animales que necesitan atención: " + e.getMessage());
        }
    }
}
