package com.Springboot.Biblioteca_backend.repository;

import com.Springboot.Biblioteca_backend.Entidades.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Long> {

    // Buscar socio por identificador (único)
    Optional<Socio> findByIdentificador(String identificador);

    // 🔹 NUEVO: Buscar socio por correo (para validar UNIQUE)
    Optional<Socio> findByCorreo(String correo);
}
