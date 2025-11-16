package com.holding.cablevision.dto;

/**
 * DTO para responder con lista de empleados disponibles para desactivar
 */
public class EmpleadoDisponibleDTO {

    private Long id;
    private String nombre;
    private String tipo; // VENDEDOR o ASESOR
    private String email;
    private Boolean activo;
    private String username;

    // Constructors
    public EmpleadoDisponibleDTO() {
    }

    public EmpleadoDisponibleDTO(Long id, String nombre, String tipo, String email, Boolean activo, String username) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.email = email;
        this.activo = activo;
        this.username = username;
    }

    // Getters and Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "EmpleadoDisponibleDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", activo=" + activo +
                '}';
    }
}
