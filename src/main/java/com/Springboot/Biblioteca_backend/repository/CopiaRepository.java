package com.Springboot.Biblioteca_backend.repository;

import com.Springboot.Biblioteca_backend.Entidades.Copia;
import com.Springboot.Biblioteca_backend.Entidades.EstadoCopia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CopiaRepository extends JpaRepository<Copia, Long> {

    //SELECT COUNT(*) FROM copia WHERE id_libro = 1 AND estado_copia = 'disponible';
    long countByLibroIdAndEstadoCopia(Long idLibro, EstadoCopia estadoCopia);

    //SELECT COUNT(*) FROM copia WHERE id_libro = 1;
    long countByLibroId(Long idLibro);
}