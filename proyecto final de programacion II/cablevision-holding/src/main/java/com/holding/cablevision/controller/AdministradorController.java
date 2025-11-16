package com.holding.cablevision.controller;

import com.holding.cablevision.dto.VendedorDTO;
import com.holding.cablevision.dto.EmpresaDTO;
import com.holding.cablevision.dto.AsesorDTO;
import com.holding.cablevision.dto.EmpresaCompletaDTO;
import com.holding.cablevision.dto.EmpleadosBatchDTO;
import com.holding.cablevision.dto.EmpleadoRegistroDTO;
import com.holding.cablevision.model.*;
import com.holding.cablevision.service.*;
import com.holding.cablevision.repository.CredencialesUsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administración", description = "API para administradores del holding")
@PreAuthorize("hasRole('ADMIN')")
public class AdministradorController {

    private final VendedorService vendedorService;
    private final EmpresaService empresaService;
    private final AsesorService asesorService;
    private final AreaMercadoService areaMercadoService;
    private final PaisService paisService;
    private final EmpresaCompletaService empresaCompletaService;
    private final CredencialesUsuarioRepository credencialesRepository;

    public AdministradorController(VendedorService vendedorService,
                                   EmpresaService empresaService,
                                   AsesorService asesorService,
                                   AreaMercadoService areaMercadoService,
                                   PaisService paisService,
                                   EmpresaCompletaService empresaCompletaService,
                                   CredencialesUsuarioRepository credencialesRepository) {
        this.vendedorService = vendedorService;
        this.empresaService = empresaService;
        this.asesorService = asesorService;
        this.areaMercadoService = areaMercadoService;
        this.paisService = paisService;
        this.empresaCompletaService = empresaCompletaService;
        this.credencialesRepository = credencialesRepository;
    }
    
    // ============ MÉTODOS AUXILIARES ============
    
    private void desactivarEmpleado(Long usuarioId, String tipoUsuario) {
        credencialesRepository.findByUsuarioIdAndTipoUsuario(usuarioId, tipoUsuario)
                .ifPresent(cred -> {
                    cred.setActivo(false);
                    credencialesRepository.save(cred);
                });
    }

    // ============ GESTIÓN DE VENDEDORES ============
    @GetMapping("/vendedores")
    @Operation(summary = "Listar todos los vendedores")
    public ResponseEntity<List<VendedorDTO>> listarVendedores() {
        List<VendedorDTO> vendedores = vendedorService.obtenerTodos();
        return ResponseEntity.ok(vendedores);
    }

    @GetMapping("/vendedores/{id}")
    @Operation(summary = "Obtener vendedor por ID")
    public ResponseEntity<VendedorDTO> obtenerVendedor(@PathVariable Long id) {
        return vendedorService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/vendedores")
    @Operation(summary = "Crear nuevo vendedor")
    public ResponseEntity<VendedorDTO> crearVendedor(@Valid @RequestBody Vendedor vendedor) {
        VendedorDTO vendedorCreado = vendedorService.guardar(vendedor);
        return ResponseEntity.ok(vendedorCreado);
    }

    @PutMapping("/vendedores/{id}")
    @Operation(summary = "Actualizar vendedor")
    public ResponseEntity<VendedorDTO> actualizarVendedor(@PathVariable Long id, 
                                                          @Valid @RequestBody Vendedor vendedor) {
        if (vendedorService.findById(id).isPresent()) {
            vendedor.setId(id);
            VendedorDTO vendedorActualizado = vendedorService.guardar(vendedor);
            return ResponseEntity.ok(vendedorActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/vendedores/{id}")
    @Operation(summary = "Eliminar vendedor")
    public ResponseEntity<?> eliminarVendedor(@PathVariable Long id) {
        if (vendedorService.findById(id).isPresent()) {
            vendedorService.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Vendedor eliminado exitosamente"));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/vendedores/captacion")
    @Operation(summary = "Realizar captación de vendedor")
    public ResponseEntity<?> realizarCaptacion(@RequestBody Map<String, Long> request) {
        Long vendedorCaptadorId = request.get("vendedorCaptadorId");
        Long vendedorCaptadoId = request.get("vendedorCaptadoId");
        
        boolean exito = vendedorService.realizarCaptacion(vendedorCaptadorId, vendedorCaptadoId);
        
        if (exito) {
            return ResponseEntity.ok(Map.of("mensaje", "Captación realizada exitosamente"));
        }
        return ResponseEntity.badRequest()
                .body(Map.of("mensaje", "No se pudo realizar la captación"));
    }

    // ============ GESTIÓN DE EMPRESAS ============
    @GetMapping("/empresas")
    @Operation(summary = "Listar todas las empresas")
    public ResponseEntity<List<Empresa>> listarEmpresas() {
        List<Empresa> empresas = empresaService.findAll();
        return ResponseEntity.ok(empresas);
    }

    @PostMapping("/empresas")
    @Operation(summary = "Crear nueva empresa")
    public ResponseEntity<Empresa> crearEmpresa(@Valid @RequestBody Empresa empresa) {
        Empresa empresaCreada = empresaService.save(empresa);
        return ResponseEntity.ok(empresaCreada);
    }

    @PutMapping("/empresas/{id}")
    @Operation(summary = "Actualizar empresa")
    public ResponseEntity<Empresa> actualizarEmpresa(@PathVariable Long id, 
                                                     @Valid @RequestBody Empresa empresa) {
        if (empresaService.findById(id).isPresent()) {
            empresa.setId(id);
            Empresa empresaActualizada = empresaService.save(empresa);
            return ResponseEntity.ok(empresaActualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/empresas/{id}")
    @Operation(summary = "Eliminar empresa")
    public ResponseEntity<?> eliminarEmpresa(@PathVariable Long id) {
        if (empresaService.findById(id).isPresent()) {
            empresaService.deleteById(id);
            return ResponseEntity.ok(Map.of("mensaje", "Empresa eliminada exitosamente"));
        }
        return ResponseEntity.notFound().build();
    }

    // ============ GESTIÓN DE ASESORES ============
    @GetMapping("/asesores")
    @Operation(summary = "Listar todos los asesores")
    public ResponseEntity<List<Asesor>> listarAsesores() {
        List<Asesor> asesores = asesorService.findAll();
        return ResponseEntity.ok(asesores);
    }

    @PostMapping("/asesores")
    @Operation(summary = "Crear nuevo asesor")
    public ResponseEntity<Asesor> crearAsesor(@Valid @RequestBody Asesor asesor) {
        Asesor asesorCreado = asesorService.save(asesor);
        return ResponseEntity.ok(asesorCreado);
    }

    @PutMapping("/asesores/{id}")
    @Operation(summary = "Actualizar asesor")
    public ResponseEntity<Asesor> actualizarAsesor(@PathVariable Long id, 
                                                   @Valid @RequestBody Asesor asesor) {
        if (asesorService.findById(id).isPresent()) {
            asesor.setId(id);
            Asesor asesorActualizado = asesorService.save(asesor);
            return ResponseEntity.ok(asesorActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/asesores/{id}")
    @Operation(summary = "Eliminar asesor")
    public ResponseEntity<?> eliminarAsesor(@PathVariable Long id) {
        if (asesorService.findById(id).isPresent()) {
            asesorService.deleteById(id);
            return ResponseEntity.ok(Map.of("mensaje", "Asesor eliminado exitosamente"));
        }
        return ResponseEntity.notFound().build();
    }

    // ============ GESTIÓN DE PAÍSES ============
    @GetMapping("/paises")
    @Operation(summary = "Listar todos los países")
    public ResponseEntity<List<Pais>> listarPaises() {
        List<Pais> paises = paisService.findAll();
        return ResponseEntity.ok(paises);
    }

    @PostMapping("/paises")
    @Operation(summary = "Crear nuevo país")
    public ResponseEntity<Pais> crearPais(@Valid @RequestBody Pais pais) {
        Pais paisCreado = paisService.save(pais);
        return ResponseEntity.ok(paisCreado);
    }

    @PutMapping("/paises/{id}")
    @Operation(summary = "Actualizar país")
    public ResponseEntity<Pais> actualizarPais(@PathVariable Long id, @Valid @RequestBody Pais pais) {
        if (paisService.findById(id).isPresent()) {
            pais.setId(id);
            Pais paisActualizado = paisService.save(pais);
            return ResponseEntity.ok(paisActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/paises/{id}")
    @Operation(summary = "Eliminar país")
    public ResponseEntity<?> eliminarPais(@PathVariable Long id) {
        if (paisService.findById(id).isPresent()) {
            paisService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ============ GESTIÓN DE ÁREAS DE MERCADO ============
    @GetMapping("/areas-mercado")
    @Operation(summary = "Listar todas las áreas de mercado")
    public ResponseEntity<List<AreaMercado>> listarAreasMercado() {
        List<AreaMercado> areas = areaMercadoService.findAll();
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/areas-mercado")
    @Operation(summary = "Crear nueva área de mercado")
    public ResponseEntity<AreaMercado> crearAreaMercado(@Valid @RequestBody AreaMercado area) {
        AreaMercado areaCreada = areaMercadoService.save(area);
        return ResponseEntity.ok(areaCreada);
    }

    @PutMapping("/areas-mercado/{id}")
    @Operation(summary = "Actualizar área de mercado")
    public ResponseEntity<AreaMercado> actualizarAreaMercado(@PathVariable Long id, @Valid @RequestBody AreaMercado area) {
        if (areaMercadoService.findById(id).isPresent()) {
            area.setId(id);
            AreaMercado areaActualizada = areaMercadoService.save(area);
            return ResponseEntity.ok(areaActualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/areas-mercado/{id}")
    @Operation(summary = "Eliminar área de mercado")
    public ResponseEntity<?> eliminarAreaMercado(@PathVariable Long id) {
        if (areaMercadoService.findById(id).isPresent()) {
            areaMercadoService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ============ CREACIÓN COMPLETA DE EMPRESA CON EMPLEADOS ============
    @PostMapping("/empresas/completa")
    @Operation(summary = "Crear empresa completa con vendedores y asesores", 
               description = "Crea una empresa y registra automáticamente todos sus empleados con sus credenciales de acceso")
    public ResponseEntity<?> crearEmpresaCompleta(@Valid @RequestBody EmpresaCompletaDTO empresaCompletaDTO) {
        try {
            EmpresaDTO empresaCreada = empresaCompletaService.crearEmpresaCompleta(empresaCompletaDTO);
            return ResponseEntity.ok(empresaCreada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/empresas/calcular-empleados/{cantidad}")
    @Operation(summary = "Calcular distribución de empleados", 
               description = "Calcula la cantidad sugerida de vendedores y asesores según las reglas de negocio")
    public ResponseEntity<Map<String, Integer>> calcularDistribucionEmpleados(@PathVariable Integer cantidad) {
        Map<String, Integer> distribucion = empresaCompletaService.calcularDistribucionEmpleados(cantidad);
        return ResponseEntity.ok(distribucion);
    }

    // ============ REGISTRO BATCH DE EMPLEADOS ============
    @PostMapping("/empresas/{empresaId}/empleados/batch")
    @Operation(summary = "Registrar múltiples empleados", 
               description = "Registra vendedores y asesores con sus credenciales de acceso. Valida reglas de negocio.")
    public ResponseEntity<?> registrarEmpleadosBatch(
            @PathVariable Long empresaId,
            @Valid @RequestBody EmpleadosBatchDTO batchDTO) {
        
        // Validar que la empresa existe
        if (empresaService.findById(empresaId).isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Empresa no encontrada"));
        }
        
        // Validar reglas de negocio
        if (!batchDTO.validarReglasNegocio()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", batchDTO.obtenerMensajeValidacion()));
        }
        
        try {
            Map<String, Object> resultado = empresaCompletaService.registrarEmpleadosBatch(empresaId, batchDTO);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al registrar empleados: " + e.getMessage()));
        }
    }
    
    @PostMapping("/empresas/{empresaId}/validar-distribucion")
    @Operation(summary = "Validar distribución de empleados", 
               description = "Valida si la cantidad de vendedores y asesores cumple con las reglas de negocio")
    public ResponseEntity<?> validarDistribucionEmpleados(
            @PathVariable Long empresaId,
            @RequestBody Map<String, Integer> distribucion) {
        
        Integer totalEmpleados = distribucion.get("total");
        Integer asesores = distribucion.get("asesores");
        Integer vendedores = distribucion.get("vendedores");
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("valido", true);
        resultado.put("mensajes", new java.util.ArrayList<String>());
        
        @SuppressWarnings("unchecked")
        List<String> mensajes = (List<String>) resultado.get("mensajes");
        
        // Regla 1: Mínimo 1 asesor
        if (asesores < 1) {
            resultado.put("valido", false);
            mensajes.add("Debe haber al menos 1 asesor");
        }
        
        // Regla 2: Si hay más de 30 empleados, máximo de asesores = vendedores / 2
        if (totalEmpleados > 30) {
            int maxAsesores = (int) Math.floor(vendedores / 2.0);
            if (asesores > maxAsesores) {
                resultado.put("valido", false);
                mensajes.add(String.format("Para %d empleados, máximo %d asesores permitidos (vendedores/2)", 
                    totalEmpleados, maxAsesores));
            }
        }
        
        // Información adicional
        resultado.put("totalEmpleados", totalEmpleados);
        resultado.put("vendedores", vendedores);
        resultado.put("asesores", asesores);
        
        if (totalEmpleados > 30) {
            resultado.put("maxAsesores", (int) Math.floor(vendedores / 2.0));
        }
        
        return ResponseEntity.ok(resultado);
    }

    // ============ REPORTES ============
    @GetMapping("/reportes/resumen-holding")
    @Operation(summary = "Obtener resumen del holding")
    public ResponseEntity<Map<String, Object>> obtenerResumenHolding() {
        Map<String, Object> resumen = Map.of(
            "totalEmpresas", empresaService.findAll().size(),
            "totalVendedores", vendedorService.findAll().size(),
            "totalAsesores", asesorService.findAll().size(),
            "totalPaises", paisService.findAll().size(),
            "totalAreas", areaMercadoService.findAll().size()
        );
        return ResponseEntity.ok(resumen);
    }

    // ============ GESTIÓN AVANZADA DE EMPRESAS Y EMPLEADOS ============
    
    @GetMapping("/empresas/{empresaId}/empleados")
    @Operation(summary = "Obtener empleados de una empresa", 
               description = "Lista todos los vendedores y asesores activos de una empresa específica")
    public ResponseEntity<?> obtenerEmpleadosDeEmpresa(@PathVariable Long empresaId) {
        try {
            List<com.holding.cablevision.dto.EmpleadoDisponibleDTO> empleados = new java.util.ArrayList<>();
            
            // Obtener vendedores
            List<com.holding.cablevision.model.Vendedor> vendedores = vendedorService.findByEmpresaId(empresaId);
            for (com.holding.cablevision.model.Vendedor v : vendedores) {
                // Buscar credenciales
                var credOpt = credencialesRepository.findByUsuarioIdAndTipoUsuario(v.getId(), "VENDEDOR");
                if (credOpt.isPresent() && credOpt.get().getActivo()) {
                    CredencialesUsuario cred = credOpt.get();
                    empleados.add(new com.holding.cablevision.dto.EmpleadoDisponibleDTO(
                        v.getId(), v.getNombre(), "VENDEDOR", cred.getEmail(), cred.getActivo(), cred.getUsername()
                    ));
                }
            }
            
            // Obtener asesores
            List<com.holding.cablevision.model.Asesor> asesores = asesorService.findByEmpresaId(empresaId);
            for (com.holding.cablevision.model.Asesor a : asesores) {
                // Buscar credenciales
                var credOpt = credencialesRepository.findByUsuarioIdAndTipoUsuario(a.getId(), "ASESOR");
                if (credOpt.isPresent() && credOpt.get().getActivo()) {
                    CredencialesUsuario cred = credOpt.get();
                    empleados.add(new com.holding.cablevision.dto.EmpleadoDisponibleDTO(
                        a.getId(), a.getNombre(), "ASESOR", cred.getEmail(), cred.getActivo(), cred.getUsername()
                    ));
                }
            }
            
            return ResponseEntity.ok(empleados);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al obtener empleados: " + e.getMessage()));
        }
    }
    
    @PutMapping("/empresas/{empresaId}/actualizar-empleados")
    @Operation(summary = "Actualizar cantidad de empleados", 
               description = "Permite aumentar o reducir la cantidad de empleados de una empresa")
    public ResponseEntity<?> actualizarEmpleados(
            @PathVariable Long empresaId,
            @Valid @RequestBody com.holding.cablevision.dto.ActualizacionEmpleadosDTO actualizacionDTO) {
        try {
            // Validar que exista la empresa
            if (empresaService.findById(empresaId).isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Empresa no encontrada"));
            }
            
            // Validar que la nueva cantidad no sea 0
            if (actualizacionDTO.getCantidadNueva() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Debe haber al menos 1 empleado en la empresa"));
            }
            
            // Si es incremento, agregar nuevos empleados
            if (actualizacionDTO.esIncremento()) {
                if (actualizacionDTO.getNuevosEmpleados() == null || actualizacionDTO.getNuevosEmpleados().isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "Debe proporcionar los datos de los nuevos empleados"));
                }
                
                // Registrar nuevos empleados
                for (com.holding.cablevision.dto.EmpleadoRegistroDTO empleadoDTO : actualizacionDTO.getNuevosEmpleados()) {
                    if ("VENDEDOR".equals(empleadoDTO.getTipo())) {
                        com.holding.cablevision.model.Vendedor vendedor = new com.holding.cablevision.model.Vendedor();
                        vendedor.setNombre(empleadoDTO.getNombre());
                        vendedor.setDireccion(empleadoDTO.getDireccion());
                        // Asignar empresa
                        com.holding.cablevision.model.Empresa empresa = empresaService.findById(empresaId).get();
                        vendedor.setEmpresa(empresa);
                        vendedorService.save(vendedor);
                        // No necesitamos setActivo aquí, las credenciales se crean activas por defecto
                    } else if ("ASESOR".equals(empleadoDTO.getTipo())) {
                        com.holding.cablevision.model.Asesor asesor = new com.holding.cablevision.model.Asesor();
                        asesor.setNombre(empleadoDTO.getNombre());
                        asesor.setDireccion(empleadoDTO.getDireccion());
                        asesor.setTitulacion(empleadoDTO.getTitulacion());
                        asesorService.save(asesor);
                        // No necesitamos setActivo aquí, las credenciales se crean activas por defecto
                    }
                }
                
                return ResponseEntity.ok(Map.of(
                    "mensaje", "Empleados agregados exitosamente",
                    "cantidadAgregada", actualizacionDTO.getNuevosEmpleados().size()
                ));
            }
            
            // Si es reducción, desactivar empleados
            if (actualizacionDTO.esReduccion()) {
                if (actualizacionDTO.getEmpleadosADesactivar() == null || actualizacionDTO.getEmpleadosADesactivar().isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "Debe seleccionar qué empleados desactivar"));
                }
                
                // Desactivar empleados seleccionados
                for (Long empleadoId : actualizacionDTO.getEmpleadosADesactivar()) {
                    // Intentar como vendedor
                    var vendedorOpt = vendedorService.findById(empleadoId);
                    if (vendedorOpt.isPresent()) {
                        desactivarEmpleado(empleadoId, "VENDEDOR");
                        continue;
                    }
                    
                    // Intentar como asesor
                    var asesorOpt = asesorService.findById(empleadoId);
                    if (asesorOpt.isPresent()) {
                        desactivarEmpleado(empleadoId, "ASESOR");
                    }
                }
                
                return ResponseEntity.ok(Map.of(
                    "mensaje", "Empleados desactivados exitosamente",
                    "cantidadDesactivada", actualizacionDTO.getEmpleadosADesactivar().size()
                ));
            }
            
            // Si no hay cambios
            return ResponseEntity.ok(Map.of("mensaje", "No hay cambios en la cantidad de empleados"));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al actualizar empleados: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/empresas/{empresaId}/con-gestion")
    @Operation(summary = "Eliminar empresa con gestión de empleados",
               description = "Elimina una empresa y permite reubicar o desactivar a sus empleados")
    public ResponseEntity<?> eliminarEmpresaConGestion(
            @PathVariable Long empresaId,
            @Valid @RequestBody com.holding.cablevision.dto.EliminacionEmpresaDTO eliminacionDTO) {
        try {
            // Validar DTO
            if (!eliminacionDTO.esValido()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Si elige REUBICAR debe especificar la empresa destino"));
            }
            
            // Validar que exista la empresa
            if (empresaService.findById(empresaId).isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Empresa no encontrada"));
            }
            
            // Obtener todos los empleados de la empresa
            List<com.holding.cablevision.model.Vendedor> vendedores = vendedorService.findByEmpresaId(empresaId);
            List<com.holding.cablevision.model.Asesor> asesores = asesorService.findByEmpresaId(empresaId);
            
            if (eliminacionDTO.getAccionEmpleados() == com.holding.cablevision.dto.EliminacionEmpresaDTO.AccionEmpleados.REUBICAR) {
                // Validar que exista la empresa destino
                if (empresaService.findById(eliminacionDTO.getEmpresaDestinoId()).isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "Empresa destino no encontrada"));
                }
                
                com.holding.cablevision.model.Empresa empresaDestino = empresaService.findById(eliminacionDTO.getEmpresaDestinoId()).get();
                
                // Reubicar vendedores
                for (com.holding.cablevision.model.Vendedor v : vendedores) {
                    v.setEmpresa(empresaDestino);
                    vendedorService.save(v);
                }
                
                // Los asesores no se asignan directamente a empresa, solo a áreas
                // Se mantienen activos
                
            } else {
                // Desactivar todos los empleados
                for (com.holding.cablevision.model.Vendedor v : vendedores) {
                    desactivarEmpleado(v.getId(), "VENDEDOR");
                }
                
                for (com.holding.cablevision.model.Asesor a : asesores) {
                    desactivarEmpleado(a.getId(), "ASESOR");
                }
            }
            
            // Eliminar la empresa
            empresaService.deleteById(empresaId);
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "Empresa eliminada exitosamente",
                "accionEmpleados", eliminacionDTO.getAccionEmpleados().toString(),
                "vendedoresAfectados", vendedores.size(),
                "asesoresAfectados", asesores.size()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al eliminar empresa: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/paises/{paisId}/con-gestion")
    @Operation(summary = "Eliminar país con gestión de empresas",
               description = "Elimina un país y permite eliminar o reubicar sus empresas")
    public ResponseEntity<?> eliminarPaisConGestion(
            @PathVariable Long paisId,
            @Valid @RequestBody com.holding.cablevision.dto.EliminacionPaisDTO eliminacionDTO) {
        try {
            // Validar DTO
            if (!eliminacionDTO.esValido()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Si elige REUBICAR debe especificar el país destino"));
            }
            
            // Validar que exista el país
            if (paisService.findById(paisId).isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "País no encontrado"));
            }
            
            // Obtener todas las empresas del país
            List<com.holding.cablevision.model.Empresa> empresas = empresaService.findByPaisSedeId(paisId);
            
            if (eliminacionDTO.getAccionEmpresas() == com.holding.cablevision.dto.EliminacionPaisDTO.AccionEmpresas.REUBICAR_EMPRESAS) {
                // Validar que exista el país destino
                if (paisService.findById(eliminacionDTO.getPaisDestinoId()).isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "País destino no encontrado"));
                }
                
                com.holding.cablevision.model.Pais paisDestino = paisService.findById(eliminacionDTO.getPaisDestinoId()).get();
                
                // Reubicar empresas
                for (com.holding.cablevision.model.Empresa empresa : empresas) {
                    empresa.setPaisSede(paisDestino);
                    empresaService.save(empresa);
                }
                
            } else {
                // Eliminar todas las empresas (y con ello, gestionar sus empleados)
                for (com.holding.cablevision.model.Empresa empresa : empresas) {
                    // Desactivar todos los empleados de cada empresa
                    List<com.holding.cablevision.model.Vendedor> vendedores = vendedorService.findByEmpresaId(empresa.getId());
                    List<com.holding.cablevision.model.Asesor> asesores = asesorService.findByEmpresaId(empresa.getId());
                    
                    for (com.holding.cablevision.model.Vendedor v : vendedores) {
                        desactivarEmpleado(v.getId(), "VENDEDOR");
                    }
                    
                    for (com.holding.cablevision.model.Asesor a : asesores) {
                        desactivarEmpleado(a.getId(), "ASESOR");
                    }
                    
                    empresaService.deleteById(empresa.getId());
                }
            }
            
            // Eliminar el país
            paisService.deleteById(paisId);
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "País eliminado exitosamente",
                "accionEmpresas", eliminacionDTO.getAccionEmpresas().toString(),
                "empresasAfectadas", empresas.size()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                                        .body(Map.of("error", "Error al eliminar país: " + e.getMessage()));
        }
    }
}
