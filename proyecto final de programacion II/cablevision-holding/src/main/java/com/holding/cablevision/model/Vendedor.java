package com.holding.cablevision.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("VENDEDOR")
public class Vendedor extends Usuario {
    
    private String codigoVendedor;
    
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
    
    @ManyToOne
    @JoinColumn(name = "vendedor_superior_id")
    private Vendedor vendedorSuperior;
    
    @OneToMany(mappedBy = "vendedorSuperior")
    private Set<Vendedor> vendedoresACargo = new HashSet<>();
    
    @OneToMany(mappedBy = "vendedor")
    private Set<Captacion> captaciones = new HashSet<>();
    
    // Setters explícitos
    public void setCodigoVendedor(String codigoVendedor) {
        this.codigoVendedor = codigoVendedor;
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    public void setVendedorSuperior(Vendedor vendedorSuperior) {
        this.vendedorSuperior = vendedorSuperior;
    }
}