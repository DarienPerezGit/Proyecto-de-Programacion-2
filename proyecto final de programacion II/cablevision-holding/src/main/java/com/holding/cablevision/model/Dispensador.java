package com.holding.cablevision.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "dispensadores")
public class Dispensador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private BigDecimal saldoDisponible;

    @Column(name = "ultima_recarga")
    private LocalDateTime ultimaRecarga;

    @Column(nullable = false)
    private boolean activo;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        ultimaRecarga = LocalDateTime.now();
    }
}