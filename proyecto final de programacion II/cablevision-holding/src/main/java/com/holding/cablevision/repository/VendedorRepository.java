package com.holding.cablevision.repository;

import com.holding.cablevision.model.Empresa;
import com.holding.cablevision.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    Optional<Vendedor> findByCodigoVendedor(String codigoVendedor);
    
    List<Vendedor> findByEmpresaId(Long empresaId);
    
    @Query("SELECT v FROM Vendedor v WHERE v.vendedorSuperior.id = :vendedorSuperiorId")
    List<Vendedor> findVendedoresACargo(Long vendedorSuperiorId);
    
    List<Vendedor> findByVendedorSuperiorId(Long vendedorSuperiorId);
    
    boolean existsByCodigoVendedor(String codigoVendedor);
    
    Long countByEmpresa(Empresa empresa);
}