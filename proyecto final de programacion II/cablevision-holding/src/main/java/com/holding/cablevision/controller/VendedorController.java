package com.holding.cablevision.controller;

import com.holding.cablevision.dto.VendedorDTO;
import com.holding.cablevision.dto.CaptacionDTO;
import com.holding.cablevision.service.VendedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendedor")
@Tag(name = "Vendedor", description = "API para vendedores del holding")
@PreAuthorize("hasRole('VENDEDOR')")
public class VendedorController {

    private final VendedorService vendedorService;

    public VendedorController(VendedorService vendedorService) {
        this.vendedorService = vendedorService;
    }

    @GetMapping("/mi-perfil")
    @Operation(summary = "Obtener datos del vendedor autenticado")
    public ResponseEntity<VendedorDTO> obtenerMiPerfil(Authentication authentication) {
        String codigoVendedor = authentication.getName();
        return vendedorService.obtenerPorCodigo(codigoVendedor)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/mi-jerarquia")
    @Operation(summary = "Obtener vendedores a cargo del vendedor autenticado")
    public ResponseEntity<List<VendedorDTO>> obtenerMiJerarquia(Authentication authentication) {
        String codigoVendedor = authentication.getName();
        
        return vendedorService.obtenerPorCodigo(codigoVendedor)
                .map(vendedor -> {
                    List<VendedorDTO> vendedoresACargo = 
                        vendedorService.obtenerVendedoresACargo(vendedor.getId());
                    return ResponseEntity.ok(vendedoresACargo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/mis-captaciones")
    @Operation(summary = "Obtener captaciones realizadas por el vendedor")
    public ResponseEntity<List<CaptacionDTO>> obtenerMisCaptaciones(Authentication authentication) {
        String codigoVendedor = authentication.getName();
        
        return vendedorService.obtenerPorCodigo(codigoVendedor)
                .map(vendedor -> {
                    List<CaptacionDTO> captaciones = 
                        vendedorService.obtenerCaptacionesRealizadas(vendedor.getId());
                    return ResponseEntity.ok(captaciones);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/captar-vendedor/{vendedorCaptadoId}")
    @Operation(summary = "Captar un nuevo vendedor")
    public ResponseEntity<?> captarVendedor(@PathVariable Long vendedorCaptadoId,
                                            Authentication authentication) {
        String codigoVendedor = authentication.getName();
        
        return vendedorService.obtenerPorCodigo(codigoVendedor)
                .map(vendedor -> {
                    boolean exito = vendedorService.realizarCaptacion(
                        vendedor.getId(), vendedorCaptadoId);
                    
                    if (exito) {
                        return ResponseEntity.ok(
                            Map.of("mensaje", "Vendedor captado exitosamente"));
                    } else {
                        return ResponseEntity.badRequest()
                            .body(Map.of("mensaje", "No se pudo captar al vendedor"));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estadisticas")
    @Operation(summary = "Obtener estadísticas del vendedor")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas(Authentication authentication) {
        String codigoVendedor = authentication.getName();
        
        return vendedorService.obtenerPorCodigo(codigoVendedor)
                .map(vendedor -> {
                    List<VendedorDTO> vendedoresACargo = 
                        vendedorService.obtenerVendedoresACargo(vendedor.getId());
                    List<CaptacionDTO> captaciones = 
                        vendedorService.obtenerCaptacionesRealizadas(vendedor.getId());
                    
                    Map<String, Object> estadisticas = Map.of(
                        "vendedoresACargo", vendedoresACargo.size(),
                        "totalCaptaciones", captaciones.size(),
                        "empresa", vendedor.getEmpresa() != null ? 
                                  vendedor.getEmpresa() : "Sin empresa asignada"
                    );
                    
                    return ResponseEntity.ok(estadisticas);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}