package com.rancho.gestorGranja.controller;

import com.rancho.gestorGranja.entity.Empleado;
import com.rancho.gestorGranja.service.impl.EmpleadoServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private EmpleadoServiceImpl empleadoService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Validar formato de email
            if (!loginRequest.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return ResponseEntity.badRequest().body("Formato de email inválido");
            }

            // Buscar empleado por email
            var empleadoOpt = empleadoService.findByEmail(loginRequest.getEmail());
            if (empleadoOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Usuario no encontrado");
            }

            Empleado empleado = empleadoOpt.get();
            
            // Verificar contraseña
            if (!empleadoService.validatePassword(loginRequest.getPassword(), empleado.getPassword())) {
                return ResponseEntity.badRequest().body("Contraseña incorrecta");
            }

            // Crear respuesta simple
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Login exitoso");
            response.put("empleado", empleado);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error en el login: " + e.getMessage());
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody Empleado empleado) {
        try {
            // Validar formato de email
            if (!empleado.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return ResponseEntity.badRequest().body("Formato de email inválido");
            }

            Empleado nuevoEmpleado = empleadoService.save(empleado);
            return ResponseEntity.ok(nuevoEmpleado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar: " + e.getMessage());
        }
    }

    public static class LoginRequest {
        @NotBlank(message = "El email es requerido")
        @Email(message = "Formato de email inválido")
        private String email;

        @NotBlank(message = "La contraseña es requerida")
        private String password;

        // Getters y setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
} 