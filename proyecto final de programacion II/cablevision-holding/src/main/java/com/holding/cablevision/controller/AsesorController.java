package com.holding.cablevision.controller;

import com.holding.cablevision.dto.AsesorDTO;
import com.holding.cablevision.model.Empresa;
import com.holding.cablevision.service.AsesorService;
import com.holding.cablevision.service.EmpresaService;
import com.holding.cablevision.service.AsesorEmpresaAreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/asesor")
@Tag(name = "Asesor", description = "API para asesores del holding")
@PreAuthorize("hasRole('ASESOR')")
public class AsesorController {

    private final AsesorService asesorService;
    private final EmpresaService empresaService;
    private final AsesorEmpresaAreaService asesorEmpresaAreaService;

    public AsesorController(AsesorService asesorService,
                           EmpresaService empresaService,
                           AsesorEmpresaAreaService asesorEmpresaAreaService) {
        this.asesorService = asesorService;
        this.empresaService = empresaService;
        this.asesorEmpresaAreaService = asesorEmpresaAreaService;
    }

    @GetMapping("/mi-perfil")
    @Operation(summary = "Obtener datos del asesor autenticado")
    public ResponseEntity<AsesorDTO> obtenerMiPerfil(Authentication authentication) {
        String codigoAsesor = authentication.getName();
        return asesorService.obtenerPorCodigo(codigoAsesor)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/mis-empresas")
    @Operation(summary = "Obtener empresas asignadas al asesor")
    public ResponseEntity<List<Empresa>> obtenerMisEmpresas(Authentication authentication) {
        String codigoAsesor = authentication.getName();
        
        return asesorService.obtenerPorCodigo(codigoAsesor)
                .map(asesor -> {
                    List<Empresa> empresas = empresaService.findByAsesorId(asesor.getId());
                    return ResponseEntity.ok(empresas);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/empresa/{empresaId}/detalles")
    @Operation(summary = "Obtener detalles de una empresa que asesora")
    public ResponseEntity<Empresa> obtenerDetallesEmpresa(@PathVariable Long empresaId,
                                                          Authentication authentication) {
        String codigoAsesor = authentication.getName();
        
        return asesorService.obtenerPorCodigo(codigoAsesor)
                .map(asesor -> {
                    // Verificar que el asesor está asignado a esta empresa
                    boolean tieneAcceso = asesorEmpresaAreaService
                        .existsByAsesorIdAndEmpresaId(asesor.getId(), empresaId);
                    
                    if (tieneAcceso) {
                        return empresaService.findById(empresaId)
                            .map(empresa -> ResponseEntity.ok(empresa))
                            .orElse(ResponseEntity.notFound().build());
                    } else {
                        return ResponseEntity.status(403).<Empresa>build(); // Forbidden
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/mis-areas")
    @Operation(summary = "Obtener áreas de mercado asignadas al asesor")
    public ResponseEntity<List<String>> obtenerMisAreas(Authentication authentication) {
        String codigoAsesor = authentication.getName();
        
        return asesorService.obtenerPorCodigo(codigoAsesor)
                .map(asesor -> {
                    // Simplificado - devolver lista vacía por ahora
                    List<String> areas = List.of();
                    return ResponseEntity.ok(areas);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estadisticas")
    @Operation(summary = "Obtener estadísticas del asesor")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas(Authentication authentication) {
        String codigoAsesor = authentication.getName();
        
        return asesorService.obtenerPorCodigo(codigoAsesor)
                .map(asesor -> {
                    // Simplificado - obtener estadísticas básicas
                    List<Empresa> empresas = empresaService.findByAsesorId(asesor.getId());
                    
                    Map<String, Object> estadisticas = Map.of(
                        "totalEmpresasAsesoradas", empresas.size(),
                        "totalAreasCobertura", 0, // Por simplicidad
                        "titulacion", asesor.getTitulacion() != null ? 
                                    asesor.getTitulacion() : "No especificada",
                        "empresasActivas", empresas.size()
                    );
                    
                    return ResponseEntity.ok(estadisticas);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}