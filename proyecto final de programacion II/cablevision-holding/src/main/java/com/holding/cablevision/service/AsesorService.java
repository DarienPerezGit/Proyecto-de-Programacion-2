package com.holding.cablevision.service;

import com.holding.cablevision.dto.AsesorDTO;
import com.holding.cablevision.model.Asesor;
import java.util.List;
import java.util.Optional;

public interface AsesorService {
    List<Asesor> findAll();
    Optional<Asesor> findById(Long id);
    Optional<Asesor> findByCodigoAsesor(String codigoAsesor);
    List<Asesor> findByTitulacion(String titulacion);
    List<Asesor> findByEmpresaId(Long empresaId);
    Asesor save(Asesor asesor);
    void deleteById(Long id);
    boolean existsByCodigoAsesor(String codigoAsesor);
    
    // Métodos adicionales para DTOs
    Optional<AsesorDTO> obtenerPorCodigo(String codigo);
}