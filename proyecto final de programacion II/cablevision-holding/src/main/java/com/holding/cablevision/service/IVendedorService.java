package com.holding.cablevision.service;

import com.holding.cablevision.dto.VendedorDTO;
import com.holding.cablevision.dto.CaptacionDTO;
import com.holding.cablevision.model.Vendedor;

import java.util.List;
import java.util.Optional;

public interface IVendedorService {
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