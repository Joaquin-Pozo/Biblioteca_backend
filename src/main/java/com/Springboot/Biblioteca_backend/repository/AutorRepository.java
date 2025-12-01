package com.Springboot.Biblioteca_backend.repository;

import com.Springboot.Biblioteca_backend.Entidades.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    // Ordenar automáticamente por nombre
    List<Autor> findAllByOrderByNombreCompletoAsc();

    List<Autor> findByNombreCompletoIgnoreCase(String nombreCompleto);
}
