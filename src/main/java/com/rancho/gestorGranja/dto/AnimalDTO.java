package com.rancho.gestorGranja.dto;

import com.rancho.gestorGranja.entity.Animal;
import com.rancho.gestorGranja.entity.Ubicacion;

/**
 * DTO para la entidad Animal que evita el problema de serialización recursiva
 */
public class AnimalDTO {
    private Long id;
    private String nombre;
    private String especie;
    private int edad;
    private int salud;
    private UbicacionSimpleDTO ubicacion;

    public AnimalDTO() {
    }

    public AnimalDTO(Animal animal) {
        this.id = animal.getId();
        this.nombre = animal.getNombre();
        if (animal.getEspecie() != null) {
            this.especie = animal.getEspecie().name();
        }
        this.edad = animal.getEdad();
        this.salud = animal.getSalud();
        
        if (animal.getUbicacion() != null) {
            this.ubicacion = new UbicacionSimpleDTO(animal.getUbicacion());
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

    public UbicacionSimpleDTO getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(UbicacionSimpleDTO ubicacion) {
        this.ubicacion = ubicacion;
    }

    // DTO simplificado para Ubicacion dentro de Animal
    public static class UbicacionSimpleDTO {
        private Long id;
        private String nombre;
        private String tipo;
        
        public UbicacionSimpleDTO(Ubicacion ubicacion) {
            this.id = ubicacion.getId();
            this.nombre = ubicacion.getNombre();
            if (ubicacion.getTipo() != null) {
                this.tipo = ubicacion.getTipo().name();
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
    }
}
