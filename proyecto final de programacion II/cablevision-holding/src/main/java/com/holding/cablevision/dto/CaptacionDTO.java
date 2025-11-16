package com.holding.cablevision.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CaptacionDTO {
    private Long id;
    private String vendedorCaptador;
    private String vendedorCaptado;
    private String empresa;
    private LocalDate fechaCaptacion;
}