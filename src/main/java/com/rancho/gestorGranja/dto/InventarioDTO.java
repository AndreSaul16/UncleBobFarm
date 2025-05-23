package com.rancho.gestorGranja.dto;

import com.rancho.gestorGranja.entity.Inventario;

/**
 * DTO para la entidad Inventario que evita el problema de serialización recursiva
 */
public class InventarioDTO {
    private Long id;
    private String nombre;
    private String tipo;
    private int cantidad;
    private int cantidadMinima;
    private String descripcion;
    private boolean necesitaReposicion;

    public InventarioDTO() {
    }

    public InventarioDTO(Inventario inventario) {
        this.id = inventario.getId();
        this.nombre = inventario.getNombre();
        if (inventario.getTipo() != null) {
            this.tipo = inventario.getTipo().name();
        }
        this.cantidad = inventario.getCantidad();
        this.cantidadMinima = inventario.getCantidadMinima();
        this.descripcion = inventario.getDescripcion();
        this.necesitaReposicion = inventario.necesitaReposicion();
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidadMinima() {
        return cantidadMinima;
    }

    public void setCantidadMinima(int cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isNecesitaReposicion() {
        return necesitaReposicion;
    }

    public void setNecesitaReposicion(boolean necesitaReposicion) {
        this.necesitaReposicion = necesitaReposicion;
    }
}
