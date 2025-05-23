package com.rancho.gestorGranja.dto;

import com.rancho.gestorGranja.entity.Actividad;
import com.rancho.gestorGranja.entity.Animal;
import com.rancho.gestorGranja.entity.Empleado;
import com.rancho.gestorGranja.entity.Inventario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para la entidad Actividad que evita el problema de serialización recursiva
 */
public class ActividadDTO {
    private Long id;
    private String tipo;
    private String descripcionTipo;
    private LocalDateTime fecha;
    private EmpleadoSimpleDTO realizadaPor;
    private List<AnimalSimpleDTO> afectaA;
    private List<InventarioSimpleDTO> itemsUtilizados;
    private int cantidadUtilizada;
    private String observaciones;

    public ActividadDTO() {
    }

    public ActividadDTO(Actividad actividad) {
        this.id = actividad.getId();
        if (actividad.getTipo() != null) {
            this.tipo = actividad.getTipo().name();
            this.descripcionTipo = actividad.getTipo().getDescripcion();
        }
        this.fecha = actividad.getFecha();
        this.cantidadUtilizada = actividad.getCantidadUtilizada();
        this.observaciones = actividad.getObservaciones();
        
        if (actividad.getRealizadaPor() != null) {
            this.realizadaPor = new EmpleadoSimpleDTO(actividad.getRealizadaPor());
        }
        
        if (actividad.getAfectaA() != null) {
            this.afectaA = actividad.getAfectaA().stream()
                    .map(AnimalSimpleDTO::new)
                    .collect(Collectors.toList());
        }
        
        if (actividad.getItemsUtilizados() != null) {
            this.itemsUtilizados = actividad.getItemsUtilizados().stream()
                    .map(InventarioSimpleDTO::new)
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcionTipo() {
        return descripcionTipo;
    }

    public void setDescripcionTipo(String descripcionTipo) {
        this.descripcionTipo = descripcionTipo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EmpleadoSimpleDTO getRealizadaPor() {
        return realizadaPor;
    }

    public void setRealizadaPor(EmpleadoSimpleDTO realizadaPor) {
        this.realizadaPor = realizadaPor;
    }

    public List<AnimalSimpleDTO> getAfectaA() {
        return afectaA;
    }

    public void setAfectaA(List<AnimalSimpleDTO> afectaA) {
        this.afectaA = afectaA;
    }

    public List<InventarioSimpleDTO> getItemsUtilizados() {
        return itemsUtilizados;
    }

    public void setItemsUtilizados(List<InventarioSimpleDTO> itemsUtilizados) {
        this.itemsUtilizados = itemsUtilizados;
    }

    public int getCantidadUtilizada() {
        return cantidadUtilizada;
    }

    public void setCantidadUtilizada(int cantidadUtilizada) {
        this.cantidadUtilizada = cantidadUtilizada;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    // DTOs simplificados para las entidades relacionadas
    public static class EmpleadoSimpleDTO {
        private Long id;
        private String nombre;
        private String rol;
        
        public EmpleadoSimpleDTO(Empleado empleado) {
            this.id = empleado.getId();
            this.nombre = empleado.getNombre();
            if (empleado.getRol() != null) {
                this.rol = empleado.getRol().name();
            }
        }

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

        public String getRol() {
            return rol;
        }

        public void setRol(String rol) {
            this.rol = rol;
        }
    }
    
    public static class AnimalSimpleDTO {
        private Long id;
        private String nombre;
        private String especie;
        private int salud;
        
        public AnimalSimpleDTO(Animal animal) {
            this.id = animal.getId();
            this.nombre = animal.getNombre();
            if (animal.getEspecie() != null) {
                this.especie = animal.getEspecie().name();
            }
            this.salud = animal.getSalud();
        }

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

        public int getSalud() {
            return salud;
        }

        public void setSalud(int salud) {
            this.salud = salud;
        }
    }
    
    public static class InventarioSimpleDTO {
        private Long id;
        private String nombre;
        private String tipo;
        
        public InventarioSimpleDTO(Inventario inventario) {
            this.id = inventario.getId();
            this.nombre = inventario.getNombre();
            if (inventario.getTipo() != null) {
                this.tipo = inventario.getTipo().name();
            }
        }

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
