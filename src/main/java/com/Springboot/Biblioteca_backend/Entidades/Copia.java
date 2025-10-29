package com.Springboot.Biblioteca_backend.Entidades;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "copia")
@lombok.Data   // genera getters, setters, toString, equals, hashCode
public class Copia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_libro", nullable = false)
    private Libro libro;

    @Column(name = "codigo_barras", nullable = false, unique = true, length = 80)
    private String codigoBarras;

    @Column(name = "fecha_adquisicion")
    private LocalDate fechaAdquisicion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_copia", nullable = false, length = 20)
    private EstadoCopia estadoCopia;
}
