package com.Springboot.Biblioteca_backend.dto;

import lombok.Data;

@Data
public class KpiDTO {

    private Long diasSinPrestamo;
    private Double porcentajeAtrasos;
    private Double porcentajeLibrosPrestados;
    private Long prestamosActivos;
    private Double porcentajeSociosActivos;   // NUEVO KPI

}
