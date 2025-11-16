package com.holding.cablevision.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "transacciones")
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipo;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column
    private String descripcion;

    @Column(nullable = false)
    private boolean exitosa;

    public enum TipoTransaccion {
        RETIRO, DEPOSITO, CONSULTA_SALDO
    }

    @PrePersist
    protected void onCreate() {
        fecha = LocalDateTime.now();
    }
}