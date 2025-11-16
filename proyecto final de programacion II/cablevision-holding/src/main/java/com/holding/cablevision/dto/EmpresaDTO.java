package com.holding.cablevision.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class EmpresaDTO {
    private Long id;
    private String nombre;
    private LocalDate fechaEntradaHolding;
    private Double facturacionAnual;
    private Integer numeroVendedores;
    private String paisSede;
    private String ciudadSede;
    private List<String> paisesOperacion;
    private List<String> areasMercado;
}