package com.holding.cablevision.service;

import java.math.BigDecimal;

public interface ISolicitudSaldoService {
    /**
     * Obtiene el saldo actual de una cuenta
     * @param numeroCuenta número de cuenta
     * @return saldo actual de la cuenta
     */
    BigDecimal obtenerSaldo(String numeroCuenta);

    /**
     * Genera un comprobante de consulta de saldo
     * @param numeroCuenta número de cuenta
     * @return true si se generó el comprobante correctamente
     */
    boolean generarComprobante(String numeroCuenta);
}