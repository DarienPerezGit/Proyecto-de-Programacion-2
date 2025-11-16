package com.holding.cablevision.service.impl;

import com.holding.cablevision.dto.*;
import com.holding.cablevision.model.*;
import com.holding.cablevision.repository.*;
import com.holding.cablevision.service.EmpresaCompletaService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class EmpresaCompletaServiceImpl implements EmpresaCompletaService {
    
    private final EmpresaRepository empresaRepository;
    private final VendedorRepository vendedorRepository;
    private final AsesorRepository asesorRepository;
    private final PaisRepository paisRepository;
    private final AreaMercadoRepository areaMercadoRepository;
    private final AsesorEmpresaAreaRepository asesorEmpresaAreaRepository;
    private final CredencialesUsuarioRepository credencialesRepository;
    private final PasswordEncoder passwordEncoder;
    
    public EmpresaCompletaServiceImpl(
            EmpresaRepository empresaRepository,
            VendedorRepository vendedorRepository,
            AsesorRepository asesorRepository,
            PaisRepository paisRepository,
            AreaMercadoRepository areaMercadoRepository,
            AsesorEmpresaAreaRepository asesorEmpresaAreaRepository,
            CredencialesUsuarioRepository credencialesRepository,
            PasswordEncoder passwordEncoder) {
        this.empresaRepository = empresaRepository;
        this.vendedorRepository = vendedorRepository;
        this.asesorRepository = asesorRepository;
        this.paisRepository = paisRepository;
        this.areaMercadoRepository = areaMercadoRepository;
        this.asesorEmpresaAreaRepository = asesorEmpresaAreaRepository;
        this.credencialesRepository = credencialesRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public EmpresaDTO crearEmpresaCompleta(EmpresaCompletaDTO dto) {
        
        // 1. Validar distribución de empleados
        int cantidadVendedores = dto.getVendedores() != null ? dto.getVendedores().size() : 0;
        int cantidadAsesores = dto.getAsesores() != null ? dto.getAsesores().size() : 0;
        
        if (!validarDistribucionEmpleados(cantidadVendedores, cantidadAsesores, dto.getCantidadEmpleados())) {
            throw new IllegalArgumentException("La distribución de empleados no cumple con las reglas de negocio");
        }
        
        // 2. Crear la empresa
        Empresa empresa = new Empresa();
        empresa.setNombre(dto.getNombre());
        empresa.setFechaEntradaHolding(dto.getFechaEntradaHolding());
        empresa.setFacturacionAnual(dto.getFacturacionAnual());
        empresa.setCiudadSede(dto.getCiudadSede());
        
        // Asignar país
        Pais pais = paisRepository.findById(dto.getPaisId())
                .orElseThrow(() -> new IllegalArgumentException("País no encontrado"));
        empresa.setPaisSede(pais);
        
        // Guardar empresa
        empresa = empresaRepository.save(empresa);
        
        // 3. Crear vendedores
        if (dto.getVendedores() != null && !dto.getVendedores().isEmpty()) {
            for (EmpleadoRegistroDTO vendedorDTO : dto.getVendedores()) {
                crearVendedor(vendedorDTO, empresa);
            }
        }
        
        // 4. Crear asesores
        if (dto.getAsesores() != null && !dto.getAsesores().isEmpty()) {
            for (EmpleadoRegistroDTO asesorDTO : dto.getAsesores()) {
                crearAsesor(asesorDTO, empresa, dto.getAreasMercadoIds());
            }
        }
        
        // 5. Convertir a DTO y retornar
        return convertirAEmpresaDTO(empresa);
    }
    
    private Vendedor crearVendedor(EmpleadoRegistroDTO dto, Empresa empresa) {
        // Crear vendedor
        Vendedor vendedor = new Vendedor();
        vendedor.setNombre(dto.getNombre());
        vendedor.setDireccion(dto.getDireccion());
        vendedor.setEmpresa(empresa);
        
        // Generar código único
        vendedor.setCodigoVendedor(generarCodigoVendedor(empresa));
        
        // Asignar vendedor superior si existe
        if (dto.getVendedorSuperiorId() != null) {
            vendedorRepository.findById(dto.getVendedorSuperiorId())
                    .ifPresent(vendedor::setVendedorSuperior);
        }
        
        // Guardar vendedor
        vendedor = vendedorRepository.save(vendedor);
        
        // Crear credenciales
        crearCredenciales(dto, vendedor.getId(), "VENDEDOR", "ROLE_VENDEDOR");
        
        return vendedor;
    }
    
    private Asesor crearAsesor(EmpleadoRegistroDTO dto, Empresa empresa, List<Long> areasMercadoIds) {
        // Crear asesor
        Asesor asesor = new Asesor();
        asesor.setNombre(dto.getNombre());
        asesor.setDireccion(dto.getDireccion());
        asesor.setTitulacion(dto.getTitulacion());
        
        // Generar código único
        asesor.setCodigoAsesor(generarCodigoAsesor());
        
        // Guardar asesor
        asesor = asesorRepository.save(asesor);
        
        // Asignar áreas de mercado al asesor para esta empresa
        List<Long> areasAsignar = dto.getAreasMercadoIds() != null ? 
                dto.getAreasMercadoIds() : areasMercadoIds;
        
        if (areasAsignar != null && !areasAsignar.isEmpty()) {
            final Asesor asesorFinal = asesor;
            final Empresa empresaFinal = empresa;
            
            for (Long areaId : areasAsignar) {
                areaMercadoRepository.findById(areaId).ifPresent(area -> {
                    AsesorEmpresaArea relacion = new AsesorEmpresaArea();
                    relacion.setAsesor(asesorFinal);
                    relacion.setEmpresa(empresaFinal);
                    relacion.setAreaMercado(area);
                    asesorEmpresaAreaRepository.save(relacion);
                });
            }
        }
        
        // Crear credenciales
        crearCredenciales(dto, asesor.getId(), "ASESOR", "ROLE_ASESOR");
        
        return asesor;
    }
    
    private void crearCredenciales(EmpleadoRegistroDTO dto, Long usuarioId, String tipoUsuario, String role) {
        // Validar que el username no exista
        if (credencialesRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario '" + dto.getUsername() + "' ya existe");
        }
        
        if (credencialesRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email '" + dto.getEmail() + "' ya está registrado");
        }
        
        CredencialesUsuario credenciales = new CredencialesUsuario();
        credenciales.setUsername(dto.getUsername());
        credenciales.setPassword(passwordEncoder.encode(dto.getPassword()));
        credenciales.setRole(role);
        credenciales.setUsuarioId(usuarioId);
        credenciales.setTipoUsuario(tipoUsuario);
        credenciales.setEmail(dto.getEmail());
        credenciales.setActivo(true);
        credenciales.setFechaCreacion(LocalDateTime.now());
        
        credencialesRepository.save(credenciales);
    }
    
    private String generarCodigoVendedor(Empresa empresa) {
        String prefijo = empresa.getNombre().substring(0, Math.min(3, empresa.getNombre().length())).toUpperCase();
        long count = vendedorRepository.countByEmpresa(empresa) + 1;
        return String.format("%s-V-%04d", prefijo, count);
    }
    
    private String generarCodigoAsesor() {
        long count = asesorRepository.count() + 1;
        return String.format("ASE-%04d", count);
    }
    
    @Override
    public Map<String, Integer> calcularDistribucionEmpleados(Integer cantidadEmpleados) {
        Map<String, Integer> distribucion = new HashMap<>();
        
        if (cantidadEmpleados <= 30) {
            // Para empresas pequeñas: mayoría vendedores, mínimo 1 asesor
            int asesores = 1;
            int vendedores = cantidadEmpleados - asesores;
            
            distribucion.put("vendedores", vendedores);
            distribucion.put("asesores", asesores);
        } else {
            // Para empresas grandes: asesores = vendedores / 2 (redondeado)
            int vendedores = (int) Math.ceil(cantidadEmpleados * 2.0 / 3.0);
            int asesores = cantidadEmpleados - vendedores;
            
            // Asegurar que asesores <= vendedores / 2
            int maxAsesores = vendedores / 2;
            if (asesores > maxAsesores) {
                asesores = maxAsesores;
                vendedores = cantidadEmpleados - asesores;
            }
            
            // Asegurar mínimo 1 asesor
            if (asesores < 1) {
                asesores = 1;
                vendedores = cantidadEmpleados - 1;
            }
            
            distribucion.put("vendedores", vendedores);
            distribucion.put("asesores", asesores);
        }
        
        return distribucion;
    }
    
    @Override
    public boolean validarDistribucionEmpleados(Integer cantidadVendedores, Integer cantidadAsesores, Integer cantidadTotal) {
        // Validar que la suma sea correcta
        if (cantidadVendedores + cantidadAsesores != cantidadTotal) {
            return false;
        }
        
        // Validar mínimo 1 asesor
        if (cantidadAsesores < 1) {
            return false;
        }
        
        // Si son más de 30 empleados, validar que asesores <= vendedores / 2
        if (cantidadTotal > 30) {
            return cantidadAsesores <= (cantidadVendedores / 2);
        }
        
        return true;
    }
    
    private EmpresaDTO convertirAEmpresaDTO(Empresa empresa) {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(empresa.getId());
        dto.setNombre(empresa.getNombre());
        dto.setFechaEntradaHolding(empresa.getFechaEntradaHolding());
        dto.setFacturacionAnual(empresa.getFacturacionAnual());
        dto.setCiudadSede(empresa.getCiudadSede());
        
        if (empresa.getPaisSede() != null) {
            dto.setPaisSede(empresa.getPaisSede().getNombre());
        }
        
        // Contar vendedores
        dto.setNumeroVendedores(vendedorRepository.countByEmpresa(empresa).intValue());
        
        // Obtener países de operación
        if (empresa.getPaisesOperacion() != null && !empresa.getPaisesOperacion().isEmpty()) {
            List<String> nombresPaises = empresa.getPaisesOperacion().stream()
                    .map(pais -> pais.getNombre())
                    .collect(java.util.stream.Collectors.toList());
            dto.setPaisesOperacion(nombresPaises);
        }
        
        // Obtener áreas de mercado
        if (empresa.getAreasMercado() != null && !empresa.getAreasMercado().isEmpty()) {
            List<String> nombresAreas = empresa.getAreasMercado().stream()
                    .map(area -> area.getNombre())
                    .collect(java.util.stream.Collectors.toList());
            dto.setAreasMercado(nombresAreas);
        }
        
        return dto;
    }
    
    @Override
    public Map<String, Object> registrarEmpleadosBatch(Long empresaId, EmpleadosBatchDTO batchDTO) {
        // Obtener la empresa
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));
        
        Map<String, Object> resultado = new HashMap<>();
        List<Map<String, Object>> vendedoresCreados = new ArrayList<>();
        List<Map<String, Object>> asesoresCreados = new ArrayList<>();
        List<String> errores = new ArrayList<>();
        
        // Procesar cada empleado
        for (EmpleadoRegistroDTO empleadoDTO : batchDTO.getEmpleados()) {
            try {
                if ("VENDEDOR".equalsIgnoreCase(empleadoDTO.getTipo())) {
                    Vendedor vendedor = crearVendedor(empleadoDTO, empresa);
                    Map<String, Object> vendedorInfo = new HashMap<>();
                    vendedorInfo.put("id", vendedor.getId());
                    vendedorInfo.put("nombre", vendedor.getNombre());
                    vendedorInfo.put("codigo", vendedor.getCodigoVendedor());
                    vendedorInfo.put("username", empleadoDTO.getUsername());
                    vendedoresCreados.add(vendedorInfo);
                    
                } else if ("ASESOR".equalsIgnoreCase(empleadoDTO.getTipo())) {
                    // Para asesores, usar las áreas del DTO si existen
                    Asesor asesor = crearAsesor(empleadoDTO, empresa, empleadoDTO.getAreasMercadoIds());
                    Map<String, Object> asesorInfo = new HashMap<>();
                    asesorInfo.put("id", asesor.getId());
                    asesorInfo.put("nombre", asesor.getNombre());
                    asesorInfo.put("codigo", asesor.getCodigoAsesor());
                    asesorInfo.put("username", empleadoDTO.getUsername());
                    asesorInfo.put("titulacion", asesor.getTitulacion());
                    asesoresCreados.add(asesorInfo);
                }
            } catch (Exception e) {
                errores.add("Error al crear " + empleadoDTO.getTipo() + " " + 
                           empleadoDTO.getNombre() + ": " + e.getMessage());
            }
        }
        
        // Preparar resultado
        resultado.put("exito", errores.isEmpty());
        resultado.put("empresaId", empresaId);
        resultado.put("empresaNombre", empresa.getNombre());
        resultado.put("vendedoresCreados", vendedoresCreados);
        resultado.put("asesoresCreados", asesoresCreados);
        resultado.put("totalVendedores", vendedoresCreados.size());
        resultado.put("totalAsesores", asesoresCreados.size());
        resultado.put("errores", errores);
        resultado.put("mensaje", errores.isEmpty() ? 
            "Empleados registrados exitosamente" : 
            "Algunos empleados no pudieron ser registrados");
        
        return resultado;
    }
}
