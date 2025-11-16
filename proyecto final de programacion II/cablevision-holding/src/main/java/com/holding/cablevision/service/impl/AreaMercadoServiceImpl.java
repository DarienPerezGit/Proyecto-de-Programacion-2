package com.holding.cablevision.service.impl;

import com.holding.cablevision.model.AreaMercado;
import com.holding.cablevision.repository.AreaMercadoRepository;
import com.holding.cablevision.service.AreaMercadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AreaMercadoServiceImpl implements AreaMercadoService {

    private final AreaMercadoRepository areaMercadoRepository;

    public AreaMercadoServiceImpl(AreaMercadoRepository areaMercadoRepository) {
        this.areaMercadoRepository = areaMercadoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AreaMercado> findAll() {
        return areaMercadoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AreaMercado> findById(Long id) {
        return areaMercadoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AreaMercado> findByNombre(String nombre) {
        return areaMercadoRepository.findByNombre(nombre);
    }

    @Override
    public AreaMercado save(AreaMercado areaMercado) {
        return areaMercadoRepository.save(areaMercado);
    }

    @Override
    public void deleteById(Long id) {
        areaMercadoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return areaMercadoRepository.existsByNombre(nombre);
    }
}