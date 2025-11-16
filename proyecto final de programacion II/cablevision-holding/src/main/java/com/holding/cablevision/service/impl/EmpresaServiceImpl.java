package com.holding.cablevision.service.impl;

import com.holding.cablevision.model.Empresa;
import com.holding.cablevision.repository.EmpresaRepository;
import com.holding.cablevision.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> findAll() {
        return empresaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Empresa> findById(Long id) {
        return empresaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> findByNombre(String nombre) {
        return empresaRepository.findByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> findByPaisSedeId(Long paisId) {
        return empresaRepository.findByPaisSedeId(paisId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> findByPaisOperacionId(Long paisId) {
        return empresaRepository.findByPaisOperacionId(paisId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> findByAreaMercadoId(Long areaMercadoId) {
        return empresaRepository.findByAreaMercadoId(areaMercadoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> findByAsesorId(Long asesorId) {
        return empresaRepository.findByAsesorId(asesorId);
    }

    @Override
    public Empresa save(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    @Override
    public void deleteById(Long id) {
        empresaRepository.deleteById(id);
    }
}