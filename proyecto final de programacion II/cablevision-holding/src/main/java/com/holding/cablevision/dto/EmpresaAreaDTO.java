package com.holding.cablevision.dto;

public class EmpresaAreaDTO {
    private Long empresaId;
    private String empresaNombre;
    private Long areaId;
    private String areaNombre;
    
    public EmpresaAreaDTO(Long empresaId, String empresaNombre, Long areaId, String areaNombre) {
        this.empresaId = empresaId;
        this.empresaNombre = empresaNombre;
        this.areaId = areaId;
        this.areaNombre = areaNombre;
    }
    
    // Getters y setters
    public Long getEmpresaId() {
        return empresaId;
    }
    
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
    
    public String getEmpresaNombre() {
        return empresaNombre;
    }
    
    public void setEmpresaNombre(String empresaNombre) {
        this.empresaNombre = empresaNombre;
    }
    
    public Long getAreaId() {
        return areaId;
    }
    
    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }
    
    public String getAreaNombre() {
        return areaNombre;
    }
    
    public void setAreaNombre(String areaNombre) {
        this.areaNombre = areaNombre;
    }
}