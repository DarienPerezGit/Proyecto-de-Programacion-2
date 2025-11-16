package com.holding.cablevision.dto;

import lombok.Data;
import java.util.List;

@Data
public class VendedorDTO {
    private Long id;
    private String codigoVendedor;
    private String nombre;
    private String direccion;
    private String empresa;
    private String vendedorSuperior;
    private List<String> vendedoresACargo;
    private Integer totalCaptaciones;
    
    // Getters explícitos para compatibilidad
    public Long getId() {
        return id;
    }
    
    public String getEmpresa() {
        return empresa;
    }
}