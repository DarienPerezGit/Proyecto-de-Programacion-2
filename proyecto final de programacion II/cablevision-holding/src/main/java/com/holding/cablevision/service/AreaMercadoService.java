package com.holding.cablevision.service;

import com.holding.cablevision.model.AreaMercado;
import java.util.List;
import java.util.Optional;

public interface AreaMercadoService {
    List<AreaMercado> findAll();
    Optional<AreaMercado> findById(Long id);
    Optional<AreaMercado> findByNombre(String nombre);
    AreaMercado save(AreaMercado areaMercado);
    void deleteById(Long id);
    boolean existsByNombre(String nombre);
}