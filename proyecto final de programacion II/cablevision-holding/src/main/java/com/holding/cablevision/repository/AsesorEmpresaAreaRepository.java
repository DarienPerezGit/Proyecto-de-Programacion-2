package com.holding.cablevision.repository;

import com.holding.cablevision.model.AsesorEmpresaArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface AsesorEmpresaAreaRepository extends JpaRepository<AsesorEmpresaArea, Long> {
    List<AsesorEmpresaArea> findByAsesorId(Long asesorId);
    
    List<AsesorEmpresaArea> findByEmpresaId(Long empresaId);
    
    List<AsesorEmpresaArea> findByAreaMercadoId(Long areaMercadoId);
    
    List<AsesorEmpresaArea> findByFechaInicioAfter(LocalDate fecha);
    
    boolean existsByAsesorIdAndEmpresaId(Long asesorId, Long empresaId);
}