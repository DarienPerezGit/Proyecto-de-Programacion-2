package com.holding.cablevision.repository;

import com.holding.cablevision.model.Asesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface AsesorRepository extends JpaRepository<Asesor, Long> {
    Optional<Asesor> findByCodigoAsesor(String codigoAsesor);
    
    List<Asesor> findByTitulacion(String titulacion);
    
    List<Asesor> findByEmpresasAreasEmpresaId(Long empresaId);
    
    boolean existsByCodigoAsesor(String codigoAsesor);
}