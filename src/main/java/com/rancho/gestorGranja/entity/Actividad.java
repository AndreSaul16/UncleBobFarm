package com.rancho.gestorGranja.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Actividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoActividad tipo;

    @NotNull
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "empleado_id")
    private Empleado realizadaPor;

    @ManyToMany
    @JoinTable(
        name = "actividad_animal",
        joinColumns = @JoinColumn(name = "actividad_id"),
        inverseJoinColumns = @JoinColumn(name = "animal_id")
    )
    private List<Animal> afectaA;

    @ManyToMany
    @JoinTable(
        name = "actividad_inventario",
        joinColumns = @JoinColumn(name = "actividad_id"),
        inverseJoinColumns = @JoinColumn(name = "inventario_id")
    )
    private List<Inventario> itemsUtilizados;

    private int cantidadUtilizada;

    private String observaciones;

    public enum TipoActividad {
        // Actividades de alimentación
        ALIMENTACION_MATUTINA("Alimentación matutina", 10, Inventario.TipoItem.ALIMENTO, 5),
        ALIMENTACION_VESPERTINA("Alimentación vespertina", 10, Inventario.TipoItem.ALIMENTO, 5),
        
        // Actividades de limpieza
        LIMPIEZA_CORRAL("Limpieza de corral", 5, Inventario.TipoItem.MATERIAL_LIMPIEZA, 2),
        LIMPIEZA_ESTABLO("Limpieza de establo", 5, Inventario.TipoItem.MATERIAL_LIMPIEZA, 3),
        
        // Actividades veterinarias
        REVISION_GENERAL("Revisión general de salud", 15, Inventario.TipoItem.MEDICINA, 1),
        VACUNACION_ANUAL("Vacunación anual", 20, Inventario.TipoItem.MEDICINA, 1),
        TRATAMIENTO_MEDICO("Tratamiento médico", 25, Inventario.TipoItem.MEDICINA, 2),
        
        // Actividades de movimiento
        TRASLADO_CORRAL("Traslado a otro corral", 0, null, 0),
        TRASLADO_ESTABLO("Traslado a establo", 0, null, 0);

        private final String descripcion;
        private final int efectoSalud;
        private final Inventario.TipoItem tipoItemRequerido;
        private final int cantidadRequerida;

        TipoActividad(String descripcion, int efectoSalud, Inventario.TipoItem tipoItemRequerido, int cantidadRequerida) {
            this.descripcion = descripcion;
            this.efectoSalud = efectoSalud;
            this.tipoItemRequerido = tipoItemRequerido;
            this.cantidadRequerida = cantidadRequerida;
        }

        public String getDescripcion() { return descripcion; }
        public int getEfectoSalud() { return efectoSalud; }
        public Inventario.TipoItem getTipoItemRequerido() { return tipoItemRequerido; }
        public int getCantidadRequerida() { return cantidadRequerida; }
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoActividad getTipo() { return tipo; }
    public void setTipo(TipoActividad tipo) { this.tipo = tipo; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Empleado getRealizadaPor() { return realizadaPor; }
    public void setRealizadaPor(Empleado realizadaPor) { this.realizadaPor = realizadaPor; }

    public List<Animal> getAfectaA() { return afectaA; }
    public void setAfectaA(List<Animal> afectaA) { this.afectaA = afectaA; }

    public List<Inventario> getItemsUtilizados() { return itemsUtilizados; }
    public void setItemsUtilizados(List<Inventario> itemsUtilizados) { this.itemsUtilizados = itemsUtilizados; }

    public int getCantidadUtilizada() { return cantidadUtilizada; }
    public void setCantidadUtilizada(int cantidadUtilizada) { this.cantidadUtilizada = cantidadUtilizada; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @PrePersist
    public void prePersist() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
        // Establecer la cantidad utilizada según el tipo de actividad
        if (cantidadUtilizada == 0) {
            cantidadUtilizada = tipo.getCantidadRequerida();
        }
    }
} 