package com.holding.cablevision.service.impl;

import com.holding.cablevision.model.AsesorEmpresaArea;
import com.holding.cablevision.model.Asesor;
import com.holding.cablevision.model.Empresa;
import com.holding.cablevision.model.AreaMercado;
import com.holding.cablevision.repository.AsesorEmpresaAreaRepository;
import com.holding.cablevision.repository.AsesorRepository;
import com.holding.cablevision.repository.EmpresaRepository;
import com.holding.cablevision.repository.AreaMercadoRepository;
import com.holding.cablevision.service.AsesorEmpresaAreaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AsesorEmpresaAreaServiceImpl implements AsesorEmpresaAreaService {

    private final AsesorEmpresaAreaRepository asesorEmpresaAreaRepository;
    private final AsesorRepository asesorRepository;
    private final EmpresaRepository empresaRepository;
    private final AreaMercadoRepository areaMercadoRepository;

    public AsesorEmpresaAreaServiceImpl(AsesorEmpresaAreaRepository asesorEmpresaAreaRepository,
                                        AsesorRepository asesorRepository,
                                        EmpresaRepository empresaRepository,
                                        AreaMercadoRepository areaMercadoRepository) {
        this.asesorEmpresaAreaRepository = asesorEmpresaAreaRepository;
        this.asesorRepository = asesorRepository;
        this.empresaRepository = empresaRepository;
        this.areaMercadoRepository = areaMercadoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsesorEmpresaArea> findAll() {
        return asesorEmpresaAreaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AsesorEmpresaArea> findById(Long id) {
        return asesorEmpresaAreaRepository.findById(id);
    }

    @Override
    public AsesorEmpresaArea save(AsesorEmpresaArea asesorEmpresaArea) {
        return asesorEmpresaAreaRepository.save(asesorEmpresaArea);
    }

    @Override
    public void deleteById(Long id) {
        asesorEmpresaAreaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsesorEmpresaArea> findByAsesorId(Long asesorId) {
        return asesorEmpresaAreaRepository.findByAsesorId(asesorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsesorEmpresaArea> findByEmpresaId(Long empresaId) {
        return asesorEmpresaAreaRepository.findByEmpresaId(empresaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsesorEmpresaArea> findByAreaMercadoId(Long areaId) {
        return asesorEmpresaAreaRepository.findByAreaMercadoId(areaId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByAsesorIdAndEmpresaId(Long asesorId, Long empresaId) {
        return asesorEmpresaAreaRepository.existsByAsesorIdAndEmpresaId(asesorId, empresaId);
    }

    @Override
    public AsesorEmpresaArea asignarAsesorEmpresaArea(Long asesorId, Long empresaId, 
                                                     Long areaId, LocalDate fechaInicio) {
        Optional<Asesor> asesor = asesorRepository.findById(asesorId);
        Optional<Empresa> empresa = empresaRepository.findById(empresaId);
        Optional<AreaMercado> area = areaMercadoRepository.findById(areaId);

        if (asesor.isPresent() && empresa.isPresent() && area.isPresent()) {
            AsesorEmpresaArea asignacion = new AsesorEmpresaArea();
            asignacion.setAsesor(asesor.get());
            asignacion.setEmpresa(empresa.get());
            asignacion.setAreaMercado(area.get());
            asignacion.setFechaInicio(fechaInicio);

            return asesorEmpresaAreaRepository.save(asignacion);
        }
        
        throw new IllegalArgumentException("Asesor, empresa o área no encontrados");
    }
}