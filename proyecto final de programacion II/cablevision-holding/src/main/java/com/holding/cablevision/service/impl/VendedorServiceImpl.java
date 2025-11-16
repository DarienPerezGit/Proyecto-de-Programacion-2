package com.holding.cablevision.service.impl;

import com.holding.cablevision.dto.VendedorDTO;
import com.holding.cablevision.dto.CaptacionDTO;
import com.holding.cablevision.model.Vendedor;
import com.holding.cablevision.model.Captacion;
import com.holding.cablevision.repository.VendedorRepository;
import com.holding.cablevision.repository.CaptacionRepository;
import com.holding.cablevision.service.VendedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class VendedorServiceImpl implements VendedorService {

    private final VendedorRepository vendedorRepository;
    private final CaptacionRepository captacionRepository;

    public VendedorServiceImpl(VendedorRepository vendedorRepository,
                               CaptacionRepository captacionRepository) {
        this.vendedorRepository = vendedorRepository;
        this.captacionRepository = captacionRepository;
    }

    // Métodos CRUD básicos existentes
    @Override
    @Transactional(readOnly = true)
    public List<Vendedor> findAll() {
        return vendedorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vendedor> findById(Long id) {
        return vendedorRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vendedor> findByCodigoVendedor(String codigoVendedor) {
        return vendedorRepository.findByCodigoVendedor(codigoVendedor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vendedor> findByEmpresaId(Long empresaId) {
        return vendedorRepository.findByEmpresaId(empresaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vendedor> findVendedoresACargo(Long vendedorSuperiorId) {
        return vendedorRepository.findVendedoresACargo(vendedorSuperiorId);
    }

    @Override
    public Vendedor save(Vendedor vendedor) {
        return vendedorRepository.save(vendedor);
    }

    @Override
    public void deleteById(Long id) {
        vendedorRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCodigoVendedor(String codigoVendedor) {
        return vendedorRepository.existsByCodigoVendedor(codigoVendedor);
    }

    // Nuevos métodos para DTOs y funcionalidades del holding
    @Override
    public List<VendedorDTO> obtenerTodos() {
        return vendedorRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VendedorDTO> obtenerPorId(Long id) {
        return vendedorRepository.findById(id)
                .map(this::convertirADTO);
    }

    @Override
    public Optional<VendedorDTO> obtenerPorCodigo(String codigo) {
        return vendedorRepository.findByCodigoVendedor(codigo)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional
    public VendedorDTO guardar(Vendedor vendedor) {
        Vendedor vendedorGuardado = vendedorRepository.save(vendedor);
        return convertirADTO(vendedorGuardado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        vendedorRepository.deleteById(id);
    }

    @Override
    public List<VendedorDTO> obtenerVendedoresDeEmpresa(Long empresaId) {
        return vendedorRepository.findByEmpresaId(empresaId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VendedorDTO> obtenerVendedoresACargo(Long vendedorId) {
        return vendedorRepository.findByVendedorSuperiorId(vendedorId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CaptacionDTO> obtenerCaptacionesRealizadas(Long vendedorId) {
        return captacionRepository.findByVendedorId(vendedorId).stream()
                .map(this::convertirCaptacionADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean realizarCaptacion(Long vendedorCaptadorId, Long vendedorCaptadoId) {
        Optional<Vendedor> captador = vendedorRepository.findById(vendedorCaptadorId);
        Optional<Vendedor> captado = vendedorRepository.findById(vendedorCaptadoId);
        
        if (captador.isPresent() && captado.isPresent()) {
            Captacion captacion = new Captacion();
            captacion.setVendedor(captador.get());
            captacion.setVendedorCaptado(captado.get());
            captacion.setFechaCaptacion(LocalDate.now());
            
            captacionRepository.save(captacion);
            
            // Actualizar la jerarquía
            Vendedor vendedorCaptado = captado.get();
            vendedorCaptado.setVendedorSuperior(captador.get());
            vendedorRepository.save(vendedorCaptado);
            
            return true;
        }
        return false;
    }

    private VendedorDTO convertirADTO(Vendedor vendedor) {
        VendedorDTO dto = new VendedorDTO();
        dto.setId(vendedor.getId());
        dto.setCodigoVendedor(vendedor.getCodigoVendedor());
        dto.setNombre(vendedor.getNombre());
        dto.setDireccion(vendedor.getDireccion());
        dto.setEmpresa(vendedor.getEmpresa() != null ? vendedor.getEmpresa().getNombre() : null);
        dto.setVendedorSuperior(vendedor.getVendedorSuperior() != null ? 
                                vendedor.getVendedorSuperior().getNombre() : null);
        dto.setVendedoresACargo(vendedor.getVendedoresACargo().stream()
                .map(Vendedor::getNombre)
                .collect(Collectors.toList()));
        dto.setTotalCaptaciones(vendedor.getCaptaciones().size());
        return dto;
    }

    private CaptacionDTO convertirCaptacionADTO(Captacion captacion) {
        CaptacionDTO dto = new CaptacionDTO();
        dto.setId(captacion.getId());
        dto.setVendedorCaptador(captacion.getVendedor().getNombre());
        dto.setVendedorCaptado(captacion.getVendedorCaptado().getNombre());
        dto.setEmpresa(captacion.getVendedor().getEmpresa().getNombre());
        dto.setFechaCaptacion(captacion.getFechaCaptacion());
        return dto;
    }
}