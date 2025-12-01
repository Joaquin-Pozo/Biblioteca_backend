package com.Springboot.Biblioteca_backend.scheduler;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PrestamoSchedulerService {

    private final JdbcTemplate jdbcTemplate;

    public PrestamoSchedulerService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // cron para actualizar prestamos atrasados todos los dias a las 00:05
    // @Scheduled(cron = "*/30 * * * * ?") // cada 30 segundos (solo para pruebas)
    // o directamente de http://localhost:8080/actualizar-atrasos
    @Scheduled(cron = "0 5 0 * * ?")
    public void ejecutarActualizacionAtrasos() {
        jdbcTemplate.execute("SELECT actualizar_prestamos_atrasados()");
        System.out.println("Procedimiento de atrasos ejecutado automáticamente");
    }
}