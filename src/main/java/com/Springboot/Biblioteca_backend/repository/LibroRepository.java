package com.Springboot.Biblioteca_backend.repository;

import com.Springboot.Biblioteca_backend.Entidades.Libro;
import org.springframework.data.jpa.repository.JpaRepository; //Spring Data JPA
// Spring Data JPA es un módulo del ecosistema Spring Framework que simplifica el acceso y manejo de bases
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Busca por coincidencia parcial (no distingue mayúsculas/minúsculas)
    //
    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    // 🔍 Buscar por nombre parcial del autor (relación ManyToOne)
    // SELECT * FROM libro l JOIN autor a ON l.id_autor = a.id WHERE LOWER(a.nombre_completo) LIKE LOWER('%nombreAutor%');
    List<Libro> findByAutorNombreCompletoContainingIgnoreCase(String nombreAutor);
}