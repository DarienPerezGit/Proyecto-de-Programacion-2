package com.holding.cablevision.service.impl;

import com.holding.cablevision.service.IAtmService;
import com.holding.cablevision.service.ICuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AtmServiceImpl implements IAtmService {

    private final ICuentaService cuentaService;

    public AtmServiceImpl(ICuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @Override
    public boolean autenticar(String numeroCuenta, String pin) {
        return cuentaService.validarPin(numeroCuenta, pin);
    }

    @Override
    public boolean validarOperacion(String tipoOperacion, Double monto) {
        if (monto != null && monto <= 0) {
            return false;
        }

        return switch (tipoOperacion.toUpperCase()) {
            case "RETIRO", "DEPOSITO", "CONSULTA" -> true;
            default -> false;
        };
    }
}