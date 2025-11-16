package com.holding.cablevision.service;

import com.holding.cablevision.dto.EmpresaCompletaDTO;
import com.holding.cablevision.dto.EmpresaDTO;
import com.holding.cablevision.dto.EmpleadosBatchDTO;

public interface EmpresaCompletaService {
    
    /**
     * Crea una empresa completa con todos sus empleados (vendedores y asesores)
     * y genera los usuarios correspondientes para cada empleado
     * 
     * @param empresaCompletaDTO DTO con todos los datos de la empresa y empleados
     * @return EmpresaDTO con la empresa creada
     */
    EmpresaDTO crearEmpresaCompleta(EmpresaCompletaDTO empresaCompletaDTO);
    
    /**
     * Registra múltiples empleados para una empresa existente
     * 
     * @param empresaId ID de la empresa
     * @param batchDTO DTO con la lista de empleados a registrar
     * @return Map con el resultado del registro
     */
    java.util.Map<String, Object> registrarEmpleadosBatch(Long empresaId, EmpleadosBatchDTO batchDTO);
    
    /**
     * Calcula la cantidad de vendedores y asesores según las reglas de negocio
     * 
     * @param cantidadEmpleados Total de empleados
     * @return Map con "vendedores" y "asesores"
     */
    java.util.Map<String, Integer> calcularDistribucionEmpleados(Integer cantidadEmpleados);
    
    /**
     * Valida que la distribución de empleados cumpla con las reglas de negocio
     * 
     * @param cantidadVendedores Cantidad de vendedores
     * @param cantidadAsesores Cantidad de asesores
     * @param cantidadTotal Total de empleados
     * @return true si cumple las reglas, false en caso contrario
     */
    boolean validarDistribucionEmpleados(Integer cantidadVendedores, Integer cantidadAsesores, Integer cantidadTotal);
}
