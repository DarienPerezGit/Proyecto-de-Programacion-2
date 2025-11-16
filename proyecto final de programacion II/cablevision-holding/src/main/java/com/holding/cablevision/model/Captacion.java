package com.holding.cablevision.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class Captacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Vendedor vendedor;
    
    @ManyToOne
    @JoinColumn(name = "vendedor_captado_id")
    private Vendedor vendedorCaptado;
    
    private LocalDate fechaCaptacion;
}