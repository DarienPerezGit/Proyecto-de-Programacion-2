package com.holding.cablevision.service;

import com.holding.cablevision.dto.AsesorDTO;
import com.holding.cablevision.model.Asesor;

import java.util.List;
import java.util.Optional;

public interface IAsesorService {
    List<AsesorDTO> obtenerTodos();
    Optional<AsesorDTO> obtenerPorId(Long id);
    Optional<AsesorDTO> obtenerPorCodigo(String codigo);
    AsesorDTO guardar(Asesor asesor);
    void eliminar(Long id);
    
    List<AsesorDTO> obtenerAsesoresPorEmpresa(Long empresaId);
    List<AsesorDTO> obtenerAsesoresPorArea(Long areaId);
    
    boolean asignarEmpresaArea(Long asesorId, Long empresaId, Long areaId);
}