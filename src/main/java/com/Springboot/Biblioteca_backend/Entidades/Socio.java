package com.Springboot.Biblioteca_backend.Entidades;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "socio")
@Data
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 12)
    private String identificador;

    @Column(nullable = false, length = 200)
    private String nombreCompleto;

    @Column(nullable = false, unique = true, length = 180)
    private String correo;

    @Column(length = 40)
    private String telefono;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDate fechaInscripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_socio", nullable = false, length = 20)
    private EstadoSocio estadoSocio;
}
