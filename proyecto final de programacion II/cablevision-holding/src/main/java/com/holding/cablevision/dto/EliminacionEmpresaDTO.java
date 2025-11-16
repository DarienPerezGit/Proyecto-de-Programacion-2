package com.holding.cablevision.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para eliminar una empresa y gestionar sus empleados
 */
public class EliminacionEmpresaDTO {

    @NotNull(message = "El ID de la empresa a eliminar es requerido")
    private Long empresaId;

    @NotNull(message = "Debe especificar la acción para los empleados")
    private AccionEmpleados accionEmpleados;

    private Long empresaDestinoId; // Requerido si accion es REUBICAR

    // Enum para las acciones
    public enum AccionEmpleados {
        REUBICAR,    // Reubicar empleados a otra empresa
        DESACTIVAR   // Desactivar todos los empleados
    }

    // Constructors
    public EliminacionEmpresaDTO() {
    }

    public EliminacionEmpresaDTO(Long empresaId, AccionEmpleados accionEmpleados) {
        this.empresaId = empresaId;
        this.accionEmpleados = accionEmpleados;
    }

    // Getters and Setters
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public AccionEmpleados getAccionEmpleados() {
        return accionEmpleados;
    }

    public void setAccionEmpleados(AccionEmpleados accionEmpleados) {
        this.accionEmpleados = accionEmpleados;
    }

    public Long getEmpresaDestinoId() {
        return empresaDestinoId;
    }

    public void setEmpresaDestinoId(Long empresaDestinoId) {
        this.empresaDestinoId = empresaDestinoId;
    }

    // Validation
    public boolean esValido() {
        if (accionEmpleados == AccionEmpleados.REUBICAR && empresaDestinoId == null) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EliminacionEmpresaDTO{" +
                "empresaId=" + empresaId +
                ", accionEmpleados=" + accionEmpleados +
                ", empresaDestinoId=" + empresaDestinoId +
                '}';
    }
}
