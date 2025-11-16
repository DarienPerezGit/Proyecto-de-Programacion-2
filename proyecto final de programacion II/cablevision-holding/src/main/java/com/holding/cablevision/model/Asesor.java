package com.holding.cablevision.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Set;
import java.util.HashSet;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("ASESOR")
public class Asesor extends Usuario {
    
    private String codigoAsesor;
    private String titulacion;
    
    @OneToMany(mappedBy = "asesor")
    private Set<AsesorEmpresaArea> empresasAreas = new HashSet<>();
    
    // Setters explícitos
    public void setCodigoAsesor(String codigoAsesor) {
        this.codigoAsesor = codigoAsesor;
    }
    
    public void setTitulacion(String titulacion) {
        this.titulacion = titulacion;
    }
}