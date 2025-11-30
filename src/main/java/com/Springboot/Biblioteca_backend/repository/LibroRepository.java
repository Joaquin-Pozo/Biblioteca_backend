package com.Springboot.Biblioteca_backend.repository;

import com.Springboot.Biblioteca_backend.Entidades.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    // ✅ Lo que ya tenías (NO se toca)
    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    List<Libro> findByAutorNombreCompletoContainingIgnoreCase(String nombreAutor);

    // ✅ NUEVO MÉTODO PARA KPI (Libro sin préstamo)
    @Query("""
        SELECT MAX(p.fechaPrestamo)
        FROM Prestamo p
    """)
    LocalDate obtenerUltimaFechaPrestamo();
}
