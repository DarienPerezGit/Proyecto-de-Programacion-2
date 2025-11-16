package com.holding.cablevision.service.impl;

import com.holding.cablevision.model.Cuenta;
import com.holding.cablevision.repository.CuentaRepository;
import com.holding.cablevision.service.ICuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class CuentaServiceImpl implements ICuentaService {

    private final CuentaRepository cuentaRepository;
    private final PasswordEncoder passwordEncoder;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, PasswordEncoder passwordEncoder) {
        this.cuentaRepository = cuentaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cuenta> buscarCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta);
    }

    @Override
    public boolean actualizarSaldo(String numeroCuenta, BigDecimal nuevoSaldo) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .map(cuenta -> {
                    cuenta.setSaldo(nuevoSaldo);
                    cuentaRepository.save(cuenta);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validarPin(String numeroCuenta, String pin) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .map(cuenta -> passwordEncoder.matches(pin, cuenta.getPin()))
                .orElse(false);
    }
}