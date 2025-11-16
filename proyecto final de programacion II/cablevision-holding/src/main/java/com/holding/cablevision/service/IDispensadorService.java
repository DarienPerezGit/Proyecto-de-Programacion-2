package com.holding.cablevision.service;

import java.math.BigDecimal;

public interface IDispensadorService {
    /**
     * Verifica la disponibilidad de efectivo en el dispensador
     * @param monto monto a verificar
     * @return true si hay suficiente efectivo disponible
     */
    boolean verificarDisponibilidad(BigDecimal monto);

    /**
     * Dispensa el efectivo solicitado
     * @param monto monto a dispensar
     * @return true si se dispensó el efectivo correctamente
     */
    boolean dispensarEfectivo(BigDecimal monto);

    /**
     * Obtiene el saldo actual del dispensador
     * @return saldo actual del dispensador
     */
    BigDecimal obtenerSaldoDispensador();
}