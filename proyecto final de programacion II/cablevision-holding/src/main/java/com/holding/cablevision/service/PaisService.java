package com.holding.cablevision.service;

import com.holding.cablevision.model.Pais;
import java.util.List;
import java.util.Optional;

public interface PaisService {
    List<Pais> findAll();
    Optional<Pais> findById(Long id);
    Optional<Pais> findByNombre(String nombre);
    Pais save(Pais pais);
    void deleteById(Long id);
    boolean existsByNombre(String nombre);
}