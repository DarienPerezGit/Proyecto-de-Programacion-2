package com.holding.cablevision.service;

import com.holding.cablevision.model.Cuenta;
import java.math.BigDecimal;
import java.util.Optional;

public interface ICuentaService {
    /**
     * Busca una cuenta por su número
     * @param numeroCuenta número de cuenta a buscar
     * @return Optional con la cuenta si existe
     */
    Optional<Cuenta> buscarCuenta(String numeroCuenta);

    /**
     * Actualiza el saldo de una cuenta
     * @param numeroCuenta número de cuenta
     * @param nuevoSaldo nuevo saldo de la cuenta
     * @return true si la actualización fue exitosa
     */
    boolean actualizarSaldo(String numeroCuenta, BigDecimal nuevoSaldo);

    /**
     * Valida el PIN de una cuenta
     * @param numeroCuenta número de cuenta
     * @param pin PIN a validar
     * @return true si el PIN es válido
     */
    boolean validarPin(String numeroCuenta, String pin);
}