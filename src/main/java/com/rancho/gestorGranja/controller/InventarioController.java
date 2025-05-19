package com.rancho.gestorGranja.controller;

import com.rancho.gestorGranja.entity.Inventario;
import com.rancho.gestorGranja.service.impl.InventarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioServiceImpl inventarioService;

    @GetMapping("/tipos")
    public List<Map<String, String>> getTiposItem() {
        return Arrays.stream(Inventario.TipoItem.values())
            .map(tipo -> Map.of(
                "codigo", tipo.name(),
                "descripcion", getDescripcionTipoItem(tipo)
            ))
            .collect(Collectors.toList());
    }

    @GetMapping
    public List<Inventario> getAllItems() {
        return inventarioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> getItemById(@PathVariable Long id) {
        return inventarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody Inventario item) {
        try {
            // Validar que el tipo de item es válido
            if (item.getTipo() == null) {
                return ResponseEntity.badRequest().body("El tipo de item es requerido");
            }

            // Validar que la cantidad mínima es razonable
            if (item.getCantidadMinima() < 0) {
                return ResponseEntity.badRequest().body("La cantidad mínima no puede ser negativa");
            }

            // Validar que la cantidad inicial es razonable
            if (item.getCantidad() < 0) {
                return ResponseEntity.badRequest().body("La cantidad inicial no puede ser negativa");
            }

            Inventario nuevoItem = inventarioService.save(item);
            return ResponseEntity.ok(nuevoItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear item: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody Inventario itemDetails) {
        try {
            var itemOpt = inventarioService.findById(id);
            if (itemOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Inventario item = itemOpt.get();
            // No permitir cambiar el tipo de item una vez creado
            itemDetails.setTipo(item.getTipo());
            
            // Actualizar los campos permitidos
            item.setNombre(itemDetails.getNombre());
            item.setDescripcion(itemDetails.getDescripcion());
            item.setCantidadMinima(itemDetails.getCantidadMinima());

            Inventario itemActualizado = inventarioService.save(item);
            return ResponseEntity.ok(itemActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar item: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        try {
            if (inventarioService.findById(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            inventarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar item: " + e.getMessage());
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> getItemsByTipo(@PathVariable Inventario.TipoItem tipo) {
        try {
            List<Inventario> items = inventarioService.findByTipo(tipo);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al buscar items por tipo: " + e.getMessage());
        }
    }

    @GetMapping("/necesitan-reposicion")
    public ResponseEntity<?> getItemsNecesitanReposicion() {
        try {
            List<Inventario> items = inventarioService.findNecesitanReposicion();
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al buscar items que necesitan reposición: " + e.getMessage());
        }
    }

    private String getDescripcionTipoItem(Inventario.TipoItem tipo) {
        return switch (tipo) {
            case ALIMENTO -> "Alimentos para animales";
            case MEDICINA -> "Medicamentos y productos veterinarios";
            case HERRAMIENTA -> "Herramientas y equipamiento";
            case MATERIAL_LIMPIEZA -> "Materiales de limpieza";
            case OTRO -> "Otros materiales";
        };
    }
} 