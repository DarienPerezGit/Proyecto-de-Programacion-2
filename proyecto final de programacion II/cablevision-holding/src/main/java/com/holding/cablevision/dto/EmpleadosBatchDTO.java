package com.holding.cablevision.dto;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
public class EmpleadosBatchDTO {
    
    @NotNull(message = "El ID de la empresa es obligatorio")
    private Long empresaId;
    
    @NotNull(message = "La cantidad total de empleados es obligatoria")
    @Min(value = 1, message = "Debe haber al menos 1 empleado")
    private Integer totalEmpleados;
    
    @NotEmpty(message = "Debe proporcionar al menos un empleado")
    @Valid
    private List<EmpleadoRegistroDTO> empleados;
    
    // Validación adicional de reglas de negocio
    public boolean validarReglasNegocio() {
        if (empleados == null || empleados.isEmpty()) {
            return false;
        }
        
        long asesores = empleados.stream()
            .filter(e -> "ASESOR".equalsIgnoreCase(e.getTipo()))
            .count();
        
        long vendedores = empleados.stream()
            .filter(e -> "VENDEDOR".equalsIgnoreCase(e.getTipo()))
            .count();
        
        // Regla 1: Debe haber al menos 1 asesor
        if (asesores < 1) {
            return false;
        }
        
        // Regla 2: Si hay más de 30 empleados, máximo de asesores = vendedores / 2
        if (totalEmpleados > 30) {
            int maxAsesores = (int) Math.floor(vendedores / 2.0);
            if (asesores > maxAsesores) {
                return false;
            }
        }
        
        return true;
    }
    
    public String obtenerMensajeValidacion() {
        if (empleados == null || empleados.isEmpty()) {
            return "Debe proporcionar al menos un empleado";
        }
        
        long asesores = empleados.stream()
            .filter(e -> "ASESOR".equalsIgnoreCase(e.getTipo()))
            .count();
        
        long vendedores = empleados.stream()
            .filter(e -> "VENDEDOR".equalsIgnoreCase(e.getTipo()))
            .count();
        
        if (asesores < 1) {
            return "Debe haber al menos 1 asesor";
        }
        
        if (totalEmpleados > 30) {
            int maxAsesores = (int) Math.floor(vendedores / 2.0);
            if (asesores > maxAsesores) {
                return String.format("Para %d empleados, máximo %d asesores permitidos (vendedores/2)", 
                    totalEmpleados, maxAsesores);
            }
        }
        
        return "Validación exitosa";
    }
}
