package com.rancho.gestorGranja.controller;

import com.rancho.gestorGranja.dto.UbicacionDTO;
import com.rancho.gestorGranja.entity.Ubicacion;
import com.rancho.gestorGranja.service.UbicacionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionController {
    @Autowired
    private UbicacionService ubicacionService;

    @GetMapping
    public ResponseEntity<?> getAllUbicaciones(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Ubicacion.TipoUbicacion tipo,
            @RequestParam(required = false) Integer nivelLimpiezaMinimo,
            @RequestParam(required = false) Boolean necesitaLimpieza,
            @RequestParam(required = false) Boolean tieneCapacidad) {
        try {
            List<Ubicacion> ubicaciones = ubicacionService.buscarUbicaciones(
                nombre, tipo, nivelLimpiezaMinimo, necesitaLimpieza, tieneCapacidad);
            List<UbicacionDTO> ubicacionDTOs = ubicaciones.stream()
                .map(UbicacionDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(ubicacionDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al buscar ubicaciones: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUbicacionById(@PathVariable Long id) {
        return ubicacionService.findById(id)
            .map(ubicacion -> ResponseEntity.ok(new UbicacionDTO(ubicacion)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createUbicacion(@Valid @RequestBody Ubicacion ubicacion) {
        try {
            Ubicacion nuevaUbicacion = ubicacionService.save(ubicacion);
            return ResponseEntity.ok(new UbicacionDTO(nuevaUbicacion));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear ubicación: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUbicacion(@PathVariable Long id, @Valid @RequestBody Ubicacion ubicacionDetails) {
        try {
            var ubicacionOpt = ubicacionService.findById(id);
            if (ubicacionOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Ubicacion ubicacion = ubicacionOpt.get();
            ubicacion.setNombre(ubicacionDetails.getNombre());
            ubicacion.setTipo(ubicacionDetails.getTipo());
            ubicacion.setCapacidad(ubicacionDetails.getCapacidad());
            
            Ubicacion ubicacionActualizada = ubicacionService.save(ubicacion);
            return ResponseEntity.ok(ubicacionActualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar ubicación: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUbicacion(@PathVariable Long id) {
        try {
            if (ubicacionService.findById(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            ubicacionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar ubicación: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/ocupacion")
    public ResponseEntity<?> getOcupacion(@PathVariable Long id) {
        try {
            var ubicacionOpt = ubicacionService.findById(id);
            if (ubicacionOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Ubicacion ubicacion = ubicacionOpt.get();
            Map<String, Object> ocupacion = Map.of(
                "id", ubicacion.getId(),
                "nombre", ubicacion.getNombre(),
                "capacidad", ubicacion.getCapacidad(),
                "ocupacionActual", ubicacion.getOcupacionActual(),
                "porcentajeOcupacion", ubicacion.getPorcentajeOcupacion(),
                "tieneCapacidadDisponible", ubicacion.tieneCapacidadDisponible()
            );
            return ResponseEntity.ok(ocupacion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener ocupación: " + e.getMessage());
        }
    }

    @GetMapping("/ocupacion")
    public ResponseEntity<?> getAllOcupacion() {
        try {
            var ocupaciones = ubicacionService.findAll().stream()
                .map(ubicacion -> Map.<String, Object>of(
                    "id", ubicacion.getId(),
                    "nombre", ubicacion.getNombre(),
                    "capacidad", ubicacion.getCapacidad(),
                    "ocupacionActual", ubicacion.getOcupacionActual(),
                    "porcentajeOcupacion", ubicacion.getPorcentajeOcupacion(),
                    "tieneCapacidadDisponible", ubicacion.tieneCapacidadDisponible()
                ))
                .collect(Collectors.toList());
            return ResponseEntity.ok(ocupaciones);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener ocupaciones: " + e.getMessage());
        }
    }

    @GetMapping("/mantenimiento")
    public ResponseEntity<?> getUbicacionesNecesitanLimpieza() {
        try {
            List<Ubicacion> ubicaciones = ubicacionService.findByNivelLimpiezaLessThan(30);
            return ResponseEntity.ok(ubicaciones);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al buscar ubicaciones que necesitan limpieza: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/limpiar")
    public ResponseEntity<?> registrarLimpieza(@PathVariable Long id) {
        try {
            var ubicacionOpt = ubicacionService.findById(id);
            if (ubicacionOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Ubicacion ubicacion = ubicacionOpt.get();
            ubicacion.registrarLimpieza();
            Ubicacion ubicacionActualizada = ubicacionService.save(ubicacion);
            return ResponseEntity.ok(ubicacionActualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar limpieza: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/mantenimiento")
    public ResponseEntity<?> registrarMantenimiento(@PathVariable Long id) {
        try {
            var ubicacionOpt = ubicacionService.findById(id);
            if (ubicacionOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Ubicacion ubicacion = ubicacionOpt.get();
            ubicacion.registrarMantenimiento();
            Ubicacion ubicacionActualizada = ubicacionService.save(ubicacion);
            return ResponseEntity.ok(ubicacionActualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar mantenimiento: " + e.getMessage());
        }
    }

    @GetMapping("/tipos")
    public ResponseEntity<?> getTiposUbicacion() {
        try {
            List<Map<String, String>> tipos = List.of(Ubicacion.TipoUbicacion.values()).stream()
                .map(tipo -> Map.of(
                    "codigo", tipo.name(),
                    "nombre", tipo.getNombre(),
                    "descripcion", tipo.getDescripcion()
                ))
                .collect(Collectors.toList());
            return ResponseEntity.ok(tipos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener tipos de ubicación: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/especies-permitidas")
    public ResponseEntity<?> getEspeciesPermitidas(@PathVariable Long id) {
        try {
            var ubicacionOpt = ubicacionService.findById(id);
            if (ubicacionOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Ubicacion ubicacion = ubicacionOpt.get();
            return ResponseEntity.ok(ubicacion.getEspeciesPermitidas());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener especies permitidas: " + e.getMessage());
        }
    }

    @GetMapping("/selector")
    public ResponseEntity<?> getUbicacionesForSelector() {
        try {
            List<Ubicacion> ubicaciones = ubicacionService.findAll();
            List<Map<String, Object>> ubicacionesSimplificadas = ubicaciones.stream()
                .map(ubicacion -> Map.of(
                    "id", ubicacion.getId(),
                    "nombre", ubicacion.getNombre(),
                    "tipo", ubicacion.getTipo().name(),
                    "capacidad", ubicacion.getCapacidad(),
                    "ocupacionActual", ubicacion.getOcupacionActual(),
                    "disponible", ubicacion.tieneCapacidadDisponible(),
                    "especiesPermitidas", ubicacion.getEspeciesPermitidas().stream()
                        .map(Enum::name)
                        .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
            return ResponseEntity.ok(ubicacionesSimplificadas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener ubicaciones para selector: " + e.getMessage());
        }
    }
}