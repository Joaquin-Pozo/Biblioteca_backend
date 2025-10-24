package com.Springboot.Biblioteca_backend.Entidades;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "prestamo")
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_copia", nullable = false)
    private Copia copia;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_socio", nullable = false)
    private Socio socio;

    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDateTime fechaPrestamo;

    @Column(name = "fecha_pactada_devolucion", nullable = false)
    private LocalDateTime fechaPactadaDevolucion;

    @Column(name = "fecha_devolucion")
    private LocalDateTime fechaDevolucion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_prestamo", nullable = false, length = 20)
    private EstadoPrestamo estadoPrestamo;

    private BigDecimal multa = BigDecimal.ZERO;

    private Boolean daniado = false;
}
