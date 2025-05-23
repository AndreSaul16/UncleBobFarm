package com.rancho.gestorGranja.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotNull(message = "La especie es requerida")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('VACA', 'CABALLO', 'GALLINA')")
    private EspeciePredefinida especie;

    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 100, message = "La edad no puede ser mayor a 100")
    private int edad;

    @Min(value = 0, message = "La salud no puede ser negativa")
    @Max(value = 100, message = "La salud no puede ser mayor a 100")
    private int salud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacion_id")
    @JsonBackReference
    private Ubicacion ubicacion;

    public enum EspeciePredefinida {
        VACA("Vaca", "Bos taurus"),
        CABALLO("Caballo", "Equus ferus caballus"),
        GALLINA("Gallina", "Gallus gallus domesticus");

        private final String nombreComun;
        private final String nombreCientifico;

        EspeciePredefinida(String nombreComun, String nombreCientifico) {
            this.nombreComun = nombreComun;
            this.nombreCientifico = nombreCientifico;
        }

        public String getNombreComun() { return nombreComun; }
        public String getNombreCientifico() { return nombreCientifico; }
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public EspeciePredefinida getEspecie() { return especie; }
    public void setEspecie(EspeciePredefinida especie) { this.especie = especie; }
    
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    
    public int getSalud() { return salud; }
    public void setSalud(int salud) { this.salud = salud; }
    
    public Ubicacion getUbicacion() { return ubicacion; }
    public void setUbicacion(Ubicacion ubicacion) { this.ubicacion = ubicacion; }

    @PrePersist
    @PreUpdate
    public void validar() {
        // Validar nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del animal es requerido");
        }

        // Validar especie y convertir valores antiguos si es necesario
        if (especie == null) {
            throw new IllegalArgumentException("La especie del animal es requerida");
        }

        // Validar edad
        if (edad < 0 || edad > 100) {
            throw new IllegalArgumentException("La edad debe estar entre 0 y 100 años");
        }

        // Validar salud
        if (salud < 0 || salud > 100) {
            throw new IllegalArgumentException("La salud debe estar entre 0 y 100");
        }

        // Si la salud es baja, lanzar advertencia
        if (salud <= 20) {
            System.out.println("¡ADVERTENCIA! El animal " + nombre + " tiene la salud baja (" + salud + "%)");
        }
    }

    public boolean necesitaAtencion() {
        return salud <= 20;
    }
}
