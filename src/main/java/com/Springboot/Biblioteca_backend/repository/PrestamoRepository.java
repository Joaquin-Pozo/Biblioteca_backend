package com.Springboot.Biblioteca_backend.repository;

import com.Springboot.Biblioteca_backend.Entidades.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> { }
