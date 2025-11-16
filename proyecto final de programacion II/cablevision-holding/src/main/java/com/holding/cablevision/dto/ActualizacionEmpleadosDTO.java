package com.holding.cablevision.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO para actualizar la cantidad de empleados de una empresa
 */
public class ActualizacionEmpleadosDTO {

    @NotNull(message = "La cantidad actual de empleados es requerida")
    private Integer cantidadActual;

    @NotNull(message = "La nueva cantidad de empleados es requerida")
    private Integer cantidadNueva;

    private List<@Valid EmpleadoRegistroDTO> nuevosEmpleados; // Si aumenta

    private List<Long> empleadosADesactivar; // Si disminuye (IDs de empleados)

    // Constructors
    public ActualizacionEmpleadosDTO() {
    }

    public ActualizacionEmpleadosDTO(Integer cantidadActual, Integer cantidadNueva) {
        this.cantidadActual = cantidadActual;
        this.cantidadNueva = cantidadNueva;
    }

    // Getters and Setters
    public Integer getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(Integer cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public Integer getCantidadNueva() {
        return cantidadNueva;
    }

    public void setCantidadNueva(Integer cantidadNueva) {
        this.cantidadNueva = cantidadNueva;
    }

    public List<EmpleadoRegistroDTO> getNuevosEmpleados() {
        return nuevosEmpleados;
    }

    public void setNuevosEmpleados(List<EmpleadoRegistroDTO> nuevosEmpleados) {
        this.nuevosEmpleados = nuevosEmpleados;
    }

    public List<Long> getEmpleadosADesactivar() {
        return empleadosADesactivar;
    }

    public void setEmpleadosADesactivar(List<Long> empleadosADesactivar) {
        this.empleadosADesactivar = empleadosADesactivar;
    }

    // Helper methods
    public boolean esIncremento() {
        return cantidadNueva > cantidadActual;
    }

    public boolean esReduccion() {
        return cantidadNueva < cantidadActual;
    }

    public int getDiferencia() {
        return Math.abs(cantidadNueva - cantidadActual);
    }

    @Override
    public String toString() {
        return "ActualizacionEmpleadosDTO{" +
                "cantidadActual=" + cantidadActual +
                ", cantidadNueva=" + cantidadNueva +
                ", tipoOperacion=" + (esIncremento() ? "INCREMENTO" : "REDUCCION") +
                ", diferencia=" + getDiferencia() +
                '}';
    }
}
