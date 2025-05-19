package com.rancho.gestorGranja.service.impl;

import com.rancho.gestorGranja.entity.Empleado;
import com.rancho.gestorGranja.repository.EmpleadoRepository;
import com.rancho.gestorGranja.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmpleadoServiceImpl implements BaseService<Empleado, Long> {
    
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Empleado> findAll() {
        return empleadoRepository.findAll();
    }

    @Override
    public Optional<Empleado> findById(Long id) {
        return empleadoRepository.findById(id);
    }

    @Override
    public Empleado save(Empleado empleado) {
        if (empleadoRepository.existsByEmail(empleado.getEmail())) {
            throw new RuntimeException("Ya existe un empleado con ese email");
        }
        return empleadoRepository.save(empleado);
    }

    @Override
    public void deleteById(Long id) {
        empleadoRepository.deleteById(id);
    }

    public Optional<Empleado> findByEmail(String email) {
        return empleadoRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return empleadoRepository.existsByEmail(email);
    }

    public boolean validatePassword(String rawPassword, String storedPassword) {
        // TODO: Implementar validación de contraseña segura
        // Por ahora, solo comparamos las contraseñas directamente
        return rawPassword.equals(storedPassword);
    }

    public List<Empleado> buscarEmpleados(String nombre, String rol, String email) {
        List<Empleado> todos = empleadoRepository.findAll();
        
        return todos.stream()
            .filter(empleado -> 
                (nombre == null || empleado.getNombre().toLowerCase().contains(nombre.toLowerCase())) &&
                (rol == null || empleado.getRol().toString().equalsIgnoreCase(rol)) &&
                (email == null || empleado.getEmail().toLowerCase().contains(email.toLowerCase()))
            )
            .collect(Collectors.toList());
    }
} 