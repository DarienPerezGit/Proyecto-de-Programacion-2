package com.holding.cablevision.service;

import com.holding.cablevision.dto.VendedorDTO;
import com.holding.cablevision.dto.CaptacionDTO;
import com.holding.cablevision.model.Vendedor;
import java.util.List;
import java.util.Optional;

public interface VendedorService {
    // Métodos CRUD básicos
    List<Vendedor> findAll();
    Optional<Vendedor> findById(Long id);
    Optional<Vendedor> findByCodigoVendedor(String codigoVendedor);
    List<Vendedor> findByEmpresaId(Long empresaId);
    List<Vendedor> findVendedoresACargo(Long vendedorSuperiorId);
    Vendedor save(Vendedor vendedor);
    void deleteById(Long id);
    boolean existsByCodigoVendedor(String codigoVendedor);
    
    // Métodos adicionales para DTOs y funcionalidades del holding
    List<VendedorDTO> obtenerTodos();
    Optional<VendedorDTO> obtenerPorId(Long id);
    Optional<VendedorDTO> obtenerPorCodigo(String codigo);
    VendedorDTO guardar(Vendedor vendedor);
    void eliminar(Long id);
    
    List<VendedorDTO> obtenerVendedoresDeEmpresa(Long empresaId);
    List<VendedorDTO> obtenerVendedoresACargo(Long vendedorId);
    List<CaptacionDTO> obtenerCaptacionesRealizadas(Long vendedorId);
    
    boolean realizarCaptacion(Long vendedorCaptadorId, Long vendedorCaptadoId);
}