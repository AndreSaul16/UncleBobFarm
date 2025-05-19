package com.rancho.gestorGranja.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotNull(message = "El tipo de item es requerido")
    @Enumerated(EnumType.STRING)
    private TipoItem tipo;

    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private int cantidad;

    @Min(value = 0, message = "La cantidad mínima no puede ser negativa")
    private int cantidadMinima; // Cantidad mínima para alertar que se necesita reponer

    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String descripcion;

    public enum TipoItem {
        ALIMENTO("Alimentos para animales", "Comida, pienso, forraje y otros alimentos para los animales"),
        MEDICINA("Medicamentos y productos veterinarios", "Vacunas, medicamentos, suplementos y productos veterinarios"),
        HERRAMIENTA("Herramientas y equipamiento", "Herramientas, equipos y utensilios para el mantenimiento"),
        MATERIAL_LIMPIEZA("Materiales de limpieza", "Productos y materiales para la limpieza de instalaciones"),
        OTRO("Otros materiales", "Materiales diversos no clasificados en las categorías anteriores");

        private final String nombre;
        private final String descripcion;

        TipoItem(String nombre, String descripcion) {
            this.nombre = nombre;
            this.descripcion = descripcion;
        }

        public String getNombre() { return nombre; }
        public String getDescripcion() { return descripcion; }
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoItem getTipo() { return tipo; }
    public void setTipo(TipoItem tipo) { this.tipo = tipo; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public int getCantidadMinima() { return cantidadMinima; }
    public void setCantidadMinima(int cantidadMinima) { this.cantidadMinima = cantidadMinima; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean necesitaReposicion() {
        return cantidad <= cantidadMinima;
    }

    @PrePersist
    @PreUpdate
    public void validar() {
        // Validar que la cantidad mínima sea razonable según el tipo
        if (cantidadMinima < 0) {
            throw new IllegalArgumentException("La cantidad mínima no puede ser negativa");
        }

        // Validar que la cantidad sea razonable según el tipo
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        // Validar que el nombre no contenga caracteres especiales
        if (nombre != null && !nombre.matches("^[a-zA-Z0-9\\s\\-\\.,]+$")) {
            throw new IllegalArgumentException("El nombre solo puede contener letras, números, espacios y los caracteres: - . ,");
        }
    }
} 