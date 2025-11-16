package com.holding.cablevision.service;

import java.math.BigDecimal;

public interface IDepositoService {
    /**
     * Realiza un depósito en una cuenta
     * @param numeroCuenta número de cuenta
     * @param monto monto a depositar
     * @return true si el depósito fue exitoso
     */
    boolean realizarDeposito(String numeroCuenta, BigDecimal monto);

    /**
     * Valida si es posible realizar un depósito
     * @param monto monto a depositar
     * @return true si el depósito es posible
     */
    boolean validarDeposito(BigDecimal monto);
}