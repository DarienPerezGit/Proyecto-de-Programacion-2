package com.holding.cablevision.service.impl;

import com.holding.cablevision.dto.AsesorDTO;
import com.holding.cablevision.model.Asesor;
import com.holding.cablevision.repository.AsesorRepository;
import com.holding.cablevision.service.AsesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AsesorServiceImpl implements AsesorService {

    private final AsesorRepository asesorRepository;

    public AsesorServiceImpl(AsesorRepository asesorRepository) {
        this.asesorRepository = asesorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asesor> findAll() {
        return asesorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Asesor> findById(Long id) {
        return asesorRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Asesor> findByCodigoAsesor(String codigoAsesor) {
        return asesorRepository.findByCodigoAsesor(codigoAsesor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asesor> findByTitulacion(String titulacion) {
        return asesorRepository.findByTitulacion(titulacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asesor> findByEmpresaId(Long empresaId) {
        // Los asesores se vinculan a empresas a través de AsesorEmpresaArea
        return asesorRepository.findByEmpresasAreasEmpresaId(empresaId);
    }

    @Override
    public Asesor save(Asesor asesor) {
        return asesorRepository.save(asesor);
    }

    @Override
    public void deleteById(Long id) {
        asesorRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCodigoAsesor(String codigoAsesor) {
        return asesorRepository.existsByCodigoAsesor(codigoAsesor);
    }

    @Override
    public Optional<AsesorDTO> obtenerPorCodigo(String codigo) {
        return asesorRepository.findByCodigoAsesor(codigo)
                .map(this::convertirADTO);
    }

    private AsesorDTO convertirADTO(Asesor asesor) {
        AsesorDTO dto = new AsesorDTO();
        dto.setId(asesor.getId());
        dto.setCodigoAsesor(asesor.getCodigoAsesor());
        dto.setNombre(asesor.getNombre());
        dto.setDireccion(asesor.getDireccion());
        dto.setTitulacion(asesor.getTitulacion());
        // Note: empresasAreas mapping would need more complex logic
        return dto;
    }
}