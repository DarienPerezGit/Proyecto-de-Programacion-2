package com.holding.cablevision.service;

import java.math.BigDecimal;

public interface IRetiroService {
    /**
     * Realiza un retiro de efectivo
     * @param numeroCuenta número de cuenta
     * @param monto monto a retirar
     * @return true si el retiro fue exitoso
     */
    boolean realizarRetiro(String numeroCuenta, BigDecimal monto);

    /**
     * Valida si es posible realizar un retiro
     * @param numeroCuenta número de cuenta
     * @param monto monto a retirar
     * @return true si el retiro es posible
     */
    boolean validarRetiro(String numeroCuenta, BigDecimal monto);
}