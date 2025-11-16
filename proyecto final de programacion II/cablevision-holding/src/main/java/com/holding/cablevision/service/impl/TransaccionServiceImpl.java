package com.holding.cablevision.service.impl;

import com.holding.cablevision.model.Transaccion;
import com.holding.cablevision.repository.CuentaRepository;
import com.holding.cablevision.repository.TransaccionRepository;
import com.holding.cablevision.service.ITransaccionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransaccionServiceImpl implements ITransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final CuentaRepository cuentaRepository;

    public TransaccionServiceImpl(
            TransaccionRepository transaccionRepository,
            CuentaRepository cuentaRepository) {
        this.transaccionRepository = transaccionRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public boolean procesarTransaccion(Transaccion transaccion) {
        try {
            transaccionRepository.save(transaccion);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaccion> obtenerHistorial(String numeroCuenta) {
        return transaccionRepository.findByCuentaNumeroCuentaOrderByFechaDesc(numeroCuenta);
    }
}