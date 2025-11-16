package com.holding.cablevision.repository;

import com.holding.cablevision.model.Dispensador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface DispensadorRepository extends JpaRepository<Dispensador, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Dispensador> findByCodigo(String codigo);
}