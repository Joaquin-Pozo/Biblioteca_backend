package com.Springboot.Biblioteca_backend.repository;

import com.Springboot.Biblioteca_backend.Entidades.Copia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CopiaRepository extends JpaRepository<Copia, Long> {
}
