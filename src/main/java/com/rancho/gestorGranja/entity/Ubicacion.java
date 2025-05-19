package com.rancho.gestorGranja.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Entity
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotNull(message = "El tipo de ubicación es requerido")
    @Enumerated(EnumType.STRING)
    private TipoUbicacion tipo;

    @Min(value = 0, message = "La capacidad no puede ser negativa")
    private int capacidad;

    @Min(value = 0, message = "La ocupación no puede ser negativa")
    private int ocupacionActual;

    @Min(value = 0, message = "El nivel de limpieza no puede ser negativo")
    @Max(value = 100, message = "El nivel de limpieza no puede ser mayor a 100")
    private int nivelLimpieza;

    @Column(name = "ultima_limpieza")
    private LocalDateTime ultimaLimpieza;

    @ElementCollection
    @CollectionTable(name = "ubicacion_especies_permitidas", 
                    joinColumns = @JoinColumn(name = "ubicacion_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "especie")
    private Set<Animal.EspeciePredefinida> especiesPermitidas;

    @OneToMany(mappedBy = "ubicacion")
    private List<Animal> animales;

    public enum TipoUbicacion {
        ESTABLO("Establo", "Para caballos y vacas"),
        GALLINERO("Gallinero", "Para gallinas"),
        CORRAL("Corral", "Área exterior para cualquier especie"),
        EXTERIOR("Exterior", "Área exterior general"),
        ALMACEN("Almacén", "Para almacenamiento de alimentos y suministros");

        private final String nombre;
        private final String descripcion;

        TipoUbicacion(String nombre, String descripcion) {
            this.nombre = nombre;
            this.descripcion = descripcion;
        }

        public String getNombre() { return nombre; }
        public String getDescripcion() { return descripcion; }

        public Set<Animal.EspeciePredefinida> getEspeciesPermitidas() {
            return switch (this) {
                case ESTABLO -> EnumSet.of(Animal.EspeciePredefinida.VACA, Animal.EspeciePredefinida.CABALLO);
                case GALLINERO -> EnumSet.of(Animal.EspeciePredefinida.GALLINA);
                case CORRAL, EXTERIOR -> EnumSet.allOf(Animal.EspeciePredefinida.class);
                case ALMACEN -> EnumSet.noneOf(Animal.EspeciePredefinida.class);
            };
        }
    }

    @PrePersist
    @PreUpdate
    public void prePersist() {
        // Inicializar tipo por defecto si es null
        if (tipo == null) {
            tipo = TipoUbicacion.EXTERIOR;
        }
        
        // Validar la entidad
        validar();
    }

    protected void validar() {
        // Validar nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ubicación es requerido");
        }

        // Validar tipo
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de ubicación es requerido");
        }

        // Validar capacidad
        if (capacidad < 0) {
            throw new IllegalArgumentException("La capacidad no puede ser negativa");
        }

        // Validar ocupación
        if (ocupacionActual < 0) {
            throw new IllegalArgumentException("La ocupación no puede ser negativa");
        }
        if (ocupacionActual > capacidad) {
            throw new IllegalArgumentException("La ocupación no puede ser mayor que la capacidad");
        }

        // Validar nivel de limpieza
        if (nivelLimpieza < 0 || nivelLimpieza > 100) {
            throw new IllegalArgumentException("El nivel de limpieza debe estar entre 0 y 100");
        }

        // Inicializar especies permitidas si no están definidas
        if (especiesPermitidas == null) {
            especiesPermitidas = tipo.getEspeciesPermitidas();
        }
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoUbicacion getTipo() { return tipo; }
    public void setTipo(TipoUbicacion tipo) { 
        this.tipo = tipo;
        // Actualizar especies permitidas según el tipo
        this.especiesPermitidas = tipo.getEspeciesPermitidas();
    }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public int getOcupacionActual() { return ocupacionActual; }
    public void setOcupacionActual(int ocupacionActual) { this.ocupacionActual = ocupacionActual; }

    public int getNivelLimpieza() { return nivelLimpieza; }
    public void setNivelLimpieza(int nivelLimpieza) { this.nivelLimpieza = nivelLimpieza; }

    public LocalDateTime getUltimaLimpieza() { return ultimaLimpieza; }
    public void setUltimaLimpieza(LocalDateTime ultimaLimpieza) { this.ultimaLimpieza = ultimaLimpieza; }

    public Set<Animal.EspeciePredefinida> getEspeciesPermitidas() { return especiesPermitidas; }
    public void setEspeciesPermitidas(Set<Animal.EspeciePredefinida> especiesPermitidas) { 
        this.especiesPermitidas = especiesPermitidas; 
    }

    public List<Animal> getAnimales() { return animales; }
    public void setAnimales(List<Animal> animales) { this.animales = animales; }

    public boolean permiteEspecie(Animal.EspeciePredefinida especie) {
        return especiesPermitidas.contains(especie);
    }

    public boolean necesitaLimpieza() {
        return nivelLimpieza < 30;
    }

    public double getPorcentajeOcupacion() {
        return capacidad > 0 ? (double) ocupacionActual / capacidad * 100 : 0;
    }

    public boolean tieneCapacidadDisponible() {
        return ocupacionActual < capacidad;
    }

    public void registrarLimpieza() {
        this.nivelLimpieza = 100;
        this.ultimaLimpieza = LocalDateTime.now();
    }

    public void registrarMantenimiento() {
        // Incrementar el nivel de limpieza en un 20%, pero no más de 100
        this.nivelLimpieza = Math.min(100, this.nivelLimpieza + 20);
        this.ultimaLimpieza = LocalDateTime.now();
    }
} 