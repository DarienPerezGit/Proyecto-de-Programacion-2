package com.holding.cablevision.service;

import com.holding.cablevision.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByNombre(String nombre);
    Usuario save(Usuario usuario);
    void deleteById(Long id);
    boolean existsByNombre(String nombre);
}