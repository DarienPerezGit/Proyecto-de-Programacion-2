package com.holding.cablevision.repository;

import com.holding.cablevision.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByCuentaIdOrderByFechaDesc(Long cuentaId);
    List<Transaccion> findByCuentaNumeroCuentaOrderByFechaDesc(String numeroCuenta);
}