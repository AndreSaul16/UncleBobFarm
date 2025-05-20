package com.rancho.gestorGranja.security;

import com.rancho.gestorGranja.entity.Empleado;
import com.rancho.gestorGranja.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Empleado empleado = empleadoRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró el empleado con email: " + email));

        return new User(
            empleado.getEmail(),
            empleado.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + empleado.getRol().name()))
        );
    }
} 