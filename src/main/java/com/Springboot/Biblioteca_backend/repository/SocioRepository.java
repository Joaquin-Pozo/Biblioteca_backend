package com.Springboot.Biblioteca_backend.repository;
import com.Springboot.Biblioteca_backend.Entidades.Autor;
import org.springframework.data.jpa.repository.Query;

import com.Springboot.Biblioteca_backend.Entidades.Socio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Long> {

    // Ordenar automáticamente por nombre
    List<Socio> findAllByOrderByNombreCompletoAsc();

    // Buscar socio por identificador (único)
    Optional<Socio> findByIdentificador(String identificador);

    // NUEVO: Buscar socio por correo (para validar UNIQUE)
    Optional<Socio> findByCorreo(String correo);

    // Total de socios
    @Query("SELECT COUNT(s) FROM Socio s")
    Long contarTotalSocios();

    // Socios activos
    @Query("SELECT COUNT(s) FROM Socio s WHERE s.estadoSocio = 'disponible'")
    Long contarSociosActivos();
}
