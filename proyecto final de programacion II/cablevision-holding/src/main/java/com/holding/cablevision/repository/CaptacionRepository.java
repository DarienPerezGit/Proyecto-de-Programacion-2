package com.holding.cablevision.repository;

import com.holding.cablevision.model.Captacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface CaptacionRepository extends JpaRepository<Captacion, Long> {
    List<Captacion> findByVendedorId(Long vendedorId);
    
    List<Captacion> findByVendedorCaptadoId(Long vendedorCaptadoId);
    
    List<Captacion> findByFechaCaptacionBetween(LocalDate fechaInicio, LocalDate fechaFin);
}