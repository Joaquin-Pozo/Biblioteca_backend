package com.Springboot.Biblioteca_backend.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class PingController {

    private final JdbcTemplate jdbcTemplate;

    // Inyección correcta del JdbcTemplate
    public PingController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Endpoint de prueba
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    // Ejecutar procedimiento manualmente desde el navegador o Postman
    // http://localhost:8080/actualizar-atrasos
    @GetMapping("/actualizar-atrasos")
    public String ejecutarProcedimiento() {
        jdbcTemplate.execute("SELECT actualizar_prestamos_atrasados()");
        return "Procedimiento ejecutado manualmente";
    }
}

