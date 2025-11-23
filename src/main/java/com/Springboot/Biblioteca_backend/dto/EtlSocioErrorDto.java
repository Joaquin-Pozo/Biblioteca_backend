package com.Springboot.Biblioteca_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EtlSocioErrorDto {
    private int fila;
    private String mensaje;
}
