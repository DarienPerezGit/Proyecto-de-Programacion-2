package com.holding.cablevision.repository;

import com.holding.cablevision.model.AreaMercado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AreaMercadoRepository extends JpaRepository<AreaMercado, Long> {
    Optional<AreaMercado> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
}