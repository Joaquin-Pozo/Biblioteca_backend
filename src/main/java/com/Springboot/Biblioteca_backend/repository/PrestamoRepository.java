package com.Springboot.Biblioteca_backend.repository;

import com.Springboot.Biblioteca_backend.Entidades.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    @Query("""
        SELECT COUNT(p)
        FROM Prestamo p
        WHERE p.estadoPrestamo = 'activo'
    """)
    Long contarPrestamosActivos();

    @Query("""
        SELECT COUNT(p)
        FROM Prestamo p
        WHERE p.fechaDevolucion < CURRENT_DATE
        AND p.estadoPrestamo = 'atrasado'
    """)
    Long contarPrestamosAtrasados();
}
