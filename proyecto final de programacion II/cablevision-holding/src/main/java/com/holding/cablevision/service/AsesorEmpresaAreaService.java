package com.holding.cablevision.service;

import com.holding.cablevision.model.AsesorEmpresaArea;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AsesorEmpresaAreaService {
    List<AsesorEmpresaArea> findAll();
    Optional<AsesorEmpresaArea> findById(Long id);
    AsesorEmpresaArea save(AsesorEmpresaArea asesorEmpresaArea);
    void deleteById(Long id);
    
    List<AsesorEmpresaArea> findByAsesorId(Long asesorId);
    List<AsesorEmpresaArea> findByEmpresaId(Long empresaId);
    List<AsesorEmpresaArea> findByAreaMercadoId(Long areaId);
    
    boolean existsByAsesorIdAndEmpresaId(Long asesorId, Long empresaId);
    
    AsesorEmpresaArea asignarAsesorEmpresaArea(Long asesorId, Long empresaId, 
                                              Long areaId, LocalDate fechaInicio);
}