package com.rancho.gestorGranja.controller;

import com.rancho.gestorGranja.entity.Empleado;
import com.rancho.gestorGranja.service.impl.EmpleadoServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoServiceImpl empleadoService;

    @GetMapping
    public List<Empleado> getAllEmpleados(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) String email) {
        
        // Si no hay filtros, devolver todos los empleados
        if (nombre == null && rol == null && email == null) {
            return empleadoService.findAll();
        }

        // Buscar por los filtros proporcionados
        return empleadoService.buscarEmpleados(nombre, rol, email);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> getEmpleadoById(@PathVariable Long id) {
        Optional<Empleado> empleado = empleadoService.findById(id);
        return empleado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createEmpleado(@Valid @RequestBody Empleado empleado) {
        try {
            // Validar formato de email
            if (!empleado.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return ResponseEntity.badRequest().body("Formato de email inválido");
            }

            // Validar que el email no esté en uso
            if (empleadoService.existsByEmail(empleado.getEmail())) {
                return ResponseEntity.badRequest().body("El email ya está registrado");
            }

            Empleado nuevoEmpleado = empleadoService.save(empleado);
            return ResponseEntity.ok(nuevoEmpleado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear empleado: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmpleado(@PathVariable Long id, @Valid @RequestBody Empleado empleadoDetails) {
        try {
            Optional<Empleado> empleadoOpt = empleadoService.findById(id);
            if (empleadoOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Empleado empleado = empleadoOpt.get();

            // Validar formato de email si se está actualizando
            if (empleadoDetails.getEmail() != null && !empleadoDetails.getEmail().equals(empleado.getEmail())) {
                if (!empleadoDetails.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    return ResponseEntity.badRequest().body("Formato de email inválido");
                }
                // Validar que el nuevo email no esté en uso
                if (empleadoService.existsByEmail(empleadoDetails.getEmail())) {
                    return ResponseEntity.badRequest().body("El email ya está registrado");
                }
            }

            // Actualizar los campos permitidos
            empleado.setNombre(empleadoDetails.getNombre());
            empleado.setEmail(empleadoDetails.getEmail());
            empleado.setRol(empleadoDetails.getRol());
            
            // No actualizamos la contraseña aquí por seguridad
            // Si se necesita actualizar la contraseña, debería ser un endpoint separado

            Empleado empleadoActualizado = empleadoService.save(empleado);
            return ResponseEntity.ok(empleadoActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar empleado: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmpleado(@PathVariable Long id) {
        try {
            if (empleadoService.findById(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            empleadoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar empleado: " + e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarEmpleados(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) String email) {
        try {
            List<Empleado> empleados = empleadoService.buscarEmpleados(nombre, rol, email);
            return ResponseEntity.ok(empleados);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al buscar empleados: " + e.getMessage());
        }
    }
} 