package com.holding.cablevision.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "credenciales_usuario")
public class CredencialesUsuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password; // Encriptada con BCrypt
    
    @Column(nullable = false)
    private String role; // ADMIN, VENDEDOR, ASESOR
    
    @Column(name = "usuario_id")
    private Long usuarioId; // ID del Vendedor o Asesor
    
    @Column(name = "tipo_usuario")
    private String tipoUsuario; // VENDEDOR, ASESOR, ADMIN
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;
    
    private String email;
}
