package com.Springboot.Biblioteca_backend.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EtlSociosResultDto {

    private int totalFilas;
    private int filasInsertadas;
    private int filasActualizadas;
    private int filasConError;
    private List<EtlSocioErrorDto> errores = new ArrayList<>();

    public void incrementarInsertadas() {
        this.filasInsertadas++;
    }

    public void incrementarActualizadas() {
        this.filasActualizadas++;
    }

    public void incrementarErrores() {
        this.filasConError++;
    }

    public void agregarError(int fila, String mensaje) {
        this.errores.add(new EtlSocioErrorDto(fila, mensaje));
    }
}
