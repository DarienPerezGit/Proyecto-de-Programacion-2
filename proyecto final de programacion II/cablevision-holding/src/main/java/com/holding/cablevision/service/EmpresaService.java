package com.holding.cablevision.service;

import com.holding.cablevision.model.Empresa;
import java.util.List;
import java.util.Optional;

public interface EmpresaService {
    List<Empresa> findAll();
    Optional<Empresa> findById(Long id);
    List<Empresa> findByNombre(String nombre);
    List<Empresa> findByPaisSedeId(Long paisId);
    List<Empresa> findByPaisOperacionId(Long paisId);
    List<Empresa> findByAreaMercadoId(Long areaMercadoId);
    List<Empresa> findByAsesorId(Long asesorId);
    Empresa save(Empresa empresa);
    void deleteById(Long id);
}