package Entidades;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "socio")
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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
