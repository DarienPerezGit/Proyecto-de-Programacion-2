package com.holding.cablevision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaSaldoDTO {
    private String numeroCuenta;
    private BigDecimal saldo;
    private String mensaje;
}