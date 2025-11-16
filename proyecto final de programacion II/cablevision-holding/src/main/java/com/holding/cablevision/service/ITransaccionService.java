package com.holding.cablevision.service;

import com.holding.cablevision.model.Transaccion;
import java.util.List;

public interface ITransaccionService {
    /**
     * Procesa una nueva transacción
     * @param transaccion objeto con los detalles de la transacción
     * @return true si la transacción fue exitosa
     */
    boolean procesarTransaccion(Transaccion transaccion);

    /**
     * Obtiene el historial de transacciones de una cuenta
     * @param numeroCuenta número de cuenta
     * @return lista de transacciones
     */
    List<Transaccion> obtenerHistorial(String numeroCuenta);
}