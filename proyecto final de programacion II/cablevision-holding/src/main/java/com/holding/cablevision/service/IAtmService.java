package com.holding.cablevision.service;

public interface IAtmService {
    /**
     * Autentica un usuario en el ATM
     * @param numeroCuenta número de cuenta del usuario
     * @param pin PIN de la cuenta
     * @return true si la autenticación es exitosa, false en caso contrario
     */
    boolean autenticar(String numeroCuenta, String pin);

    /**
     * Valida si una operación puede ser realizada
     * @param tipoOperacion tipo de operación a realizar
     * @param monto monto de la operación (si aplica)
     * @return true si la operación es válida, false en caso contrario
     */
    boolean validarOperacion(String tipoOperacion, Double monto);
}