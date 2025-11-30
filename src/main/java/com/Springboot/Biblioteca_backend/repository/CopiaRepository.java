package com.Springboot.Biblioteca_backend.repository;

import com.Springboot.Biblioteca_backend.Entidades.Copia;
import com.Springboot.Biblioteca_backend.Entidades.EstadoCopia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CopiaRepository extends JpaRepository<Copia, Long> {

    //SELECT COUNT(*) FROM copia WHERE id_libro = 1 AND estado_copia = 'disponible';
    long countByLibroIdAndEstadoCopia(Long idLibro, EstadoCopia estadoCopia);

    //SELECT COUNT(*) FROM copia WHERE id_libro = 1;
    long countByLibroId(Long idLibro);

    @Query("""
        SELECT COUNT(c)
        FROM Copia c
        WHERE c.estadoCopia = 'prestado'
    """)
    Long contarCopiasPrestadas();
}