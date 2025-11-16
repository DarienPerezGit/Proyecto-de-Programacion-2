package com.holding.cablevision.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;
import java.util.HashSet;

@Data
@Entity
public class Pais {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private Double pib;
    private Long numeroHabitantes;
    private String capital;
    
    @OneToMany(mappedBy = "paisSede")
    private Set<Empresa> empresasSede = new HashSet<>();
    
    @ManyToMany(mappedBy = "paisesOperacion")
    private Set<Empresa> empresasOperando = new HashSet<>();
    
    // Setters explícitos
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setPib(Double pib) {
        this.pib = pib;
    }
    
    public void setNumeroHabitantes(Long numeroHabitantes) {
        this.numeroHabitantes = numeroHabitantes;
    }
    
    public void setCapital(String capital) {
        this.capital = capital;
    }
}