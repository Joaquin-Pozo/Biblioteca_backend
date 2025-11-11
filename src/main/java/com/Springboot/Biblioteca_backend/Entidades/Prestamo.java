package com.Springboot.Biblioteca_backend.Entidades;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "prestamo")
@Data
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
    private LocalDate fechaPrestamo;

    @Column(name = "fecha_pactada_devolucion", nullable = false)
    private LocalDate fechaPactadaDevolucion;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_prestamo", nullable = false, length = 20)
    private EstadoPrestamo estadoPrestamo;

    private BigDecimal multa = BigDecimal.ZERO;

    private Boolean daniado = false;
}
