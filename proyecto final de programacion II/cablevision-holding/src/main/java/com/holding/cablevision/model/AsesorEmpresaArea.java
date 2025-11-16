package com.holding.cablevision.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class AsesorEmpresaArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "asesor_id")
    private Asesor asesor;
    
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
    
    @ManyToOne
    @JoinColumn(name = "area_mercado_id")
    private AreaMercado areaMercado;
    
    private LocalDate fechaInicio;
    
    // Setters explícitos
    public void setAsesor(Asesor asesor) {
        this.asesor = asesor;
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    public void setAreaMercado(AreaMercado areaMercado) {
        this.areaMercado = areaMercado;
    }
    
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
}