package com.holding.cablevision.repository;

import com.holding.cablevision.model.CredencialesUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredencialesUsuarioRepository extends JpaRepository<CredencialesUsuario, Long> {
    
    Optional<CredencialesUsuario> findByUsername(String username);
    
    Optional<CredencialesUsuario> findByEmail(String email);
    
    Optional<CredencialesUsuario> findByUsuarioIdAndTipoUsuario(Long usuarioId, String tipoUsuario);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
