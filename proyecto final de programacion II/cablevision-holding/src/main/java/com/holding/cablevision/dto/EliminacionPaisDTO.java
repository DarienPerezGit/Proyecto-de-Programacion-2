package com.holding.cablevision.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para eliminar un país y gestionar sus empresas
 */
public class EliminacionPaisDTO {

    @NotNull(message = "El ID del país a eliminar es requerido")
    private Long paisId;

    @NotNull(message = "Debe especificar la acción para las empresas")
    private AccionEmpresas accionEmpresas;

    private Long paisDestinoId; // Requerido si accion es REUBICAR

    // Enum para las acciones
    public enum AccionEmpresas {
        ELIMINAR_EMPRESAS,  // Eliminar todas las empresas del país
        REUBICAR_EMPRESAS   // Reubicar empresas a otro país
    }

    // Constructors
    public EliminacionPaisDTO() {
    }

    public EliminacionPaisDTO(Long paisId, AccionEmpresas accionEmpresas) {
        this.paisId = paisId;
        this.accionEmpresas = accionEmpresas;
    }

    // Getters and Setters
    public Long getPaisId() {
        return paisId;
    }

    public void setPaisId(Long paisId) {
        this.paisId = paisId;
    }

    public AccionEmpresas getAccionEmpresas() {
        return accionEmpresas;
    }

    public void setAccionEmpresas(AccionEmpresas accionEmpresas) {
        this.accionEmpresas = accionEmpresas;
    }

    public Long getPaisDestinoId() {
        return paisDestinoId;
    }

    public void setPaisDestinoId(Long paisDestinoId) {
        this.paisDestinoId = paisDestinoId;
    }

    // Validation
    public boolean esValido() {
        if (accionEmpresas == AccionEmpresas.REUBICAR_EMPRESAS && paisDestinoId == null) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EliminacionPaisDTO{" +
                "paisId=" + paisId +
                ", accionEmpresas=" + accionEmpresas +
                ", paisDestinoId=" + paisDestinoId +
                '}';
    }
}
