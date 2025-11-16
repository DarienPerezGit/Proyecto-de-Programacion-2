package com.holding.cablevision.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

@Data
@Entity
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private LocalDate fechaEntradaHolding;
    private Double facturacionAnual;
    private Integer numeroVendedores;
    private String ciudadSede;
    
    @ManyToOne
    @JoinColumn(name = "pais_sede_id")
    private Pais paisSede;
    
    @ManyToMany
    @JoinTable(
        name = "empresa_pais_operacion",
        joinColumns = @JoinColumn(name = "empresa_id"),
        inverseJoinColumns = @JoinColumn(name = "pais_id")
    )
    private Set<Pais> paisesOperacion = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
        name = "empresa_area_mercado",
        joinColumns = @JoinColumn(name = "empresa_id"),
        inverseJoinColumns = @JoinColumn(name = "area_mercado_id")
    )
    private Set<AreaMercado> areasMercado = new HashSet<>();
    
    @OneToMany(mappedBy = "empresa")
    private Set<Vendedor> vendedores = new HashSet<>();
    
    @OneToMany(mappedBy = "empresa")
    private Set<AsesorEmpresaArea> asesores = new HashSet<>();
    
    // Setters explícitos
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setFechaEntradaHolding(LocalDate fechaEntradaHolding) {
        this.fechaEntradaHolding = fechaEntradaHolding;
    }
    
    public void setFacturacionAnual(Double facturacionAnual) {
        this.facturacionAnual = facturacionAnual;
    }
    
    public void setNumeroVendedores(Integer numeroVendedores) {
        this.numeroVendedores = numeroVendedores;
    }
    
    public void setPaisSede(Pais paisSede) {
        this.paisSede = paisSede;
    }
    
    public void setCiudadSede(String ciudadSede) {
        this.ciudadSede = ciudadSede;
    }
    
    // Getters para colecciones
    public Set<Pais> getPaisesOperacion() {
        return paisesOperacion;
    }
    
    public Set<AreaMercado> getAreasMercado() {
        return areasMercado;
    }
}