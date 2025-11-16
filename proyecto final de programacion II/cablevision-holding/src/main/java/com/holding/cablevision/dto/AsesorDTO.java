package com.holding.cablevision.dto;

import java.util.List;

public class AsesorDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private String codigoAsesor;
    private String titulacion;
    private List<EmpresaAreaDTO> empresasAreas;
    
    public AsesorDTO() {}
    
    public AsesorDTO(Long id, String nombre, String direccion, String codigoAsesor, String titulacion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.codigoAsesor = codigoAsesor;
        this.titulacion = titulacion;
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
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getCodigoAsesor() {
        return codigoAsesor;
    }
    
    public void setCodigoAsesor(String codigoAsesor) {
        this.codigoAsesor = codigoAsesor;
    }
    
    public String getTitulacion() {
        return titulacion;
    }
    
    public void setTitulacion(String titulacion) {
        this.titulacion = titulacion;
    }
    
    public List<EmpresaAreaDTO> getEmpresasAreas() {
        return empresasAreas;
    }
    
    public void setEmpresasAreas(List<EmpresaAreaDTO> empresasAreas) {
        this.empresasAreas = empresasAreas;
    }
}