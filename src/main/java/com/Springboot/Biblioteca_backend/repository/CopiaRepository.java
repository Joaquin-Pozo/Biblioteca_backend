package com.Springboot.Biblioteca_backend.repository;

import com.Springboot.Biblioteca_backend.Entidades.Copia;
import com.Springboot.Biblioteca_backend.Entidades.EstadoCopia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CopiaRepository extends JpaRepository<Copia, Long> {

    long countByLibroIdAndEstadoCopia(Long idLibro, EstadoCopia estadoCopia);

    long countByLibroId(Long idLibro);
}