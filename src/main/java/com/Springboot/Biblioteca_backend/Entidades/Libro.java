package com.Springboot.Biblioteca_backend.Entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "libro")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_autor", nullable = false)
    private Autor autor;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(nullable = false)
    private int stock;

    @Column(length = 80)
    private String categoria;

    @Column(length = 120)
    private String editorial;

    @Column(name = "anio_publicacion")
    private Integer anioPublicacion;
}
