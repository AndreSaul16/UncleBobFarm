package com.rancho.gestorGranja.dto;

import com.rancho.gestorGranja.entity.Animal;
import com.rancho.gestorGranja.entity.Ubicacion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO para la entidad Ubicacion que evita el problema de serialización recursiva
 */
public class UbicacionDTO {
    private Long id;
    private String nombre;
    private String tipo;
    private int capacidad;
    private int ocupacionActual;
    private int nivelLimpieza;
    private LocalDateTime ultimaLimpieza;
    private Set<String> especiesPermitidas;
    private List<AnimalSimpleDTO> animales;

    public UbicacionDTO() {
    }

    public UbicacionDTO(Ubicacion ubicacion) {
        this.id = ubicacion.getId();
        this.nombre = ubicacion.getNombre();
        this.tipo = ubicacion.getTipo().name();
        this.capacidad = ubicacion.getCapacidad();
        this.ocupacionActual = ubicacion.getOcupacionActual();
        this.nivelLimpieza = ubicacion.getNivelLimpieza();
        this.ultimaLimpieza = ubicacion.getUltimaLimpieza();
        
        if (ubicacion.getEspeciesPermitidas() != null) {
            this.especiesPermitidas = ubicacion.getEspeciesPermitidas().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
        }

        if (ubicacion.getAnimales() != null) {
            this.animales = ubicacion.getAnimales().stream()
                .map(AnimalSimpleDTO::new)
                .collect(Collectors.toList());
        }
    }
    
    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getOcupacionActual() {
        return ocupacionActual;
    }

    public void setOcupacionActual(int ocupacionActual) {
        this.ocupacionActual = ocupacionActual;
    }

    public int getNivelLimpieza() {
        return nivelLimpieza;
    }

    public void setNivelLimpieza(int nivelLimpieza) {
        this.nivelLimpieza = nivelLimpieza;
    }

    public LocalDateTime getUltimaLimpieza() {
        return ultimaLimpieza;
    }

    public void setUltimaLimpieza(LocalDateTime ultimaLimpieza) {
        this.ultimaLimpieza = ultimaLimpieza;
    }

    public Set<String> getEspeciesPermitidas() {
        return especiesPermitidas;
    }

    public void setEspeciesPermitidas(Set<String> especiesPermitidas) {
        this.especiesPermitidas = especiesPermitidas;
    }

    public List<AnimalSimpleDTO> getAnimales() {
        return animales;
    }

    public void setAnimales(List<AnimalSimpleDTO> animales) {
        this.animales = animales;
    }

    // DTO simplificado para Animal dentro de Ubicacion
    public static class AnimalSimpleDTO {
        private Long id;
        private String nombre;
        private String especie;
        private int edad;
        private int salud;

        public AnimalSimpleDTO(Animal animal) {
            this.id = animal.getId();
            this.nombre = animal.getNombre();
            this.especie = animal.getEspecie().name();
            this.edad = animal.getEdad();
            this.salud = animal.getSalud();
        }
        
        // Getters y setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getEspecie() {
            return especie;
        }

        public void setEspecie(String especie) {
            this.especie = especie;
        }

        public int getEdad() {
            return edad;
        }

        public void setEdad(int edad) {
            this.edad = edad;
        }

        public int getSalud() {
            return salud;
        }

        public void setSalud(int salud) {
            this.salud = salud;
        }
    }
}
