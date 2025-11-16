package com.holding.cablevision.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;
import java.util.HashSet;

@Data
@Entity
public class AreaMercado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String descripcion;
    
    @ManyToMany(mappedBy = "areasMercado")
    private Set<Empresa> empresas = new HashSet<>();
    
    @OneToMany(mappedBy = "areaMercado")
    private Set<AsesorEmpresaArea> asesores = new HashSet<>();
    
    // Setters explícitos
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}