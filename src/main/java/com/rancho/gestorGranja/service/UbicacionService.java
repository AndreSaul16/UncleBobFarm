package com.rancho.gestorGranja.service;

import com.rancho.gestorGranja.entity.Ubicacion;
import java.util.List;

public interface UbicacionService extends BaseService<Ubicacion, Long> {
    // Métodos específicos de Ubicacion
    List<Ubicacion> buscarUbicaciones(String nombre, Ubicacion.TipoUbicacion tipo, 
                                    Integer nivelLimpiezaMinimo, Boolean necesitaLimpieza, 
                                    Boolean tieneCapacidad);
    List<Ubicacion> findByNivelLimpiezaLessThan(int nivelLimpieza);
    List<Ubicacion> findByTipo(Ubicacion.TipoUbicacion tipo);
} 