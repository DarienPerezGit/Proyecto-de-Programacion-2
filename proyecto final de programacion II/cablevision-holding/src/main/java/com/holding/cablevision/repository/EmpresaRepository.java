package com.holding.cablevision.repository;

import com.holding.cablevision.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    List<Empresa> findByNombre(String nombre);
    Optional<Empresa> findFirstByNombre(String nombre);
    
    @Query("SELECT e FROM Empresa e WHERE e.paisSede.id = :paisId")
    List<Empresa> findByPaisSedeId(Long paisId);
    
    @Query("SELECT e FROM Empresa e JOIN e.paisesOperacion p WHERE p.id = :paisId")
    List<Empresa> findByPaisOperacionId(Long paisId);
    
    @Query("SELECT e FROM Empresa e JOIN e.areasMercado a WHERE a.id = :areaMercadoId")
    List<Empresa> findByAreaMercadoId(Long areaMercadoId);
    
    @Query("SELECT DISTINCT aea.empresa FROM AsesorEmpresaArea aea WHERE aea.asesor.id = :asesorId")
    List<Empresa> findByAsesorId(Long asesorId);
}