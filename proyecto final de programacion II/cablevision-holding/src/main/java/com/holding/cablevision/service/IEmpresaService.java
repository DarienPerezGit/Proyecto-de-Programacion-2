package com.holding.cablevision.service;

import com.holding.cablevision.dto.EmpresaDTO;
import com.holding.cablevision.model.Empresa;

import java.util.List;
import java.util.Optional;

public interface IEmpresaService {
    List<EmpresaDTO> obtenerTodas();
    Optional<EmpresaDTO> obtenerPorId(Long id);
    EmpresaDTO guardar(Empresa empresa);
    void eliminar(Long id);
    
    List<EmpresaDTO> obtenerEmpresasPorPais(Long paisId);
    List<EmpresaDTO> obtenerEmpresasPorArea(Long areaId);
    
    boolean asignarAreaMercado(Long empresaId, Long areaId);
    boolean asignarPaisOperacion(Long empresaId, Long paisId);
}