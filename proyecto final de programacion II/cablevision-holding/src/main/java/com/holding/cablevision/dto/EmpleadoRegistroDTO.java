package com.holding.cablevision.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
public class EmpleadoRegistroDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    private String email;
    
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;
    
    // Usuario y contraseña para login
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 20, message = "El usuario debe tener entre 4 y 20 caracteres")
    private String username;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
    
    // Campos específicos para asesores
    private String titulacion;
    private List<Long> areasMercadoIds; // Áreas asignadas al asesor
    
    // Campos específicos para vendedores
    private Long vendedorSuperiorId; // ID del vendedor superior (opcional)
    
    // Tipo de empleado
    @NotBlank(message = "El tipo de empleado es obligatorio")
    @Pattern(regexp = "VENDEDOR|ASESOR", message = "El tipo debe ser VENDEDOR o ASESOR")
    private String tipo; // "VENDEDOR" o "ASESOR"
}
