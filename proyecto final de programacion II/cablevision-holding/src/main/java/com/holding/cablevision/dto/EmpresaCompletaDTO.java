package com.holding.cablevision.dto;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
public class EmpresaCompletaDTO {
    
    // Datos de la empresa
    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String nombre;
    
    @NotNull(message = "La fecha de entrada es obligatoria")
    private LocalDate fechaEntradaHolding;
    
    @NotNull(message = "La facturación anual es obligatoria")
    @Positive(message = "La facturación debe ser positiva")
    private Double facturacionAnual;
    
    @NotNull(message = "El ID del país es obligatorio")
    private Long paisId;
    
    private String ciudadSede;
    
    @NotNull(message = "La cantidad de empleados es obligatoria")
    @Min(value = 1, message = "Debe haber al menos 1 empleado")
    private Integer cantidadEmpleados;
    
    // Listas de áreas de mercado
    private List<Long> areasMercadoIds;
    
    // Datos de vendedores
    @Valid
    @NotEmpty(message = "Debe registrar al menos un vendedor")
    private List<EmpleadoRegistroDTO> vendedores;
    
    // Datos de asesores
    @Valid
    private List<EmpleadoRegistroDTO> asesores;
}
