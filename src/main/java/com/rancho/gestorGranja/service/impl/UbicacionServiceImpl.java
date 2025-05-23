package com.rancho.gestorGranja.service.impl;

import com.rancho.gestorGranja.entity.Ubicacion;
import com.rancho.gestorGranja.repository.UbicacionRepository;
import com.rancho.gestorGranja.service.UbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UbicacionServiceImpl extends BaseServiceImpl<Ubicacion, Long, UbicacionRepository> implements UbicacionService {
    
    @Autowired
    public UbicacionServiceImpl(UbicacionRepository repository) {
        super(repository);
    }

    @Override
    public Ubicacion save(Ubicacion ubicacion) {
        ubicacion.prePersist(); // Usar el método público que incluye la validación
        return super.save(ubicacion);
    }

    @Override
    public List<Ubicacion> buscarUbicaciones(String nombre, Ubicacion.TipoUbicacion tipo, 
                                           Integer nivelLimpiezaMinimo, Boolean necesitaLimpieza, 
                                           Boolean tieneCapacidad) {
        return repository.findAll().stream()
            .filter(ubicacion -> {
                // Filtrar por nombre (si se proporciona)
                if (nombre != null && !nombre.isEmpty()) {
                    if (!ubicacion.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                        return false;
                    }
                }

                // Filtrar por tipo (si se proporciona)
                if (tipo != null && ubicacion.getTipo() != tipo) {
                    return false;
                }

                // Filtrar por nivel de limpieza mínimo (si se proporciona)
                if (nivelLimpiezaMinimo != null && ubicacion.getNivelLimpieza() < nivelLimpiezaMinimo) {
                    return false;
                }

                // Filtrar por necesidad de limpieza (si se proporciona)
                if (necesitaLimpieza != null && ubicacion.necesitaLimpieza() != necesitaLimpieza) {
                    return false;
                }

                // Filtrar por capacidad disponible (si se proporciona)
                if (tieneCapacidad != null && ubicacion.tieneCapacidadDisponible() != tieneCapacidad) {
                    return false;
                }

                return true;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<Ubicacion> findByNivelLimpiezaLessThan(int nivelLimpieza) {
        return repository.findAll().stream()
            .filter(ubicacion -> ubicacion.getNivelLimpieza() < nivelLimpieza)
            .collect(Collectors.toList());
    }

    @Override
    public List<Ubicacion> findByTipo(Ubicacion.TipoUbicacion tipo) {
        return repository.findAll().stream()
            .filter(ubicacion -> ubicacion.getTipo() == tipo)
            .collect(Collectors.toList());
    }
} 