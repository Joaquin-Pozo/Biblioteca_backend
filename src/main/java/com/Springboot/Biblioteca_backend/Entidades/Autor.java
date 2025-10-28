package com.Springboot.Biblioteca_backend.Entidades;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "autor")
@lombok.Data
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_completo", nullable = false, length = 200)
    private String nombreCompleto;

    @Column(length = 60)
    private String nacionalidad;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    public Long getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getNacionalidad() { return nacionalidad; }
    public java.time.LocalDate getFechaNacimiento() { return fechaNacimiento; }

}
