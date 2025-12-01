package com.Springboot.Biblioteca_backend.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseProcedureInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseProcedureInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {

        String sql = """
            CREATE OR REPLACE FUNCTION actualizar_prestamos_atrasados()
            RETURNS void
            LANGUAGE plpgsql
            AS $$
            BEGIN
                UPDATE prestamo
                SET estado_prestamo = 'atrasado'
                WHERE fecha_pactada_devolucion < CURRENT_DATE
                  AND estado_prestamo = 'activo';
            END;
            $$;
        """;

        jdbcTemplate.execute(sql);

        System.out.println("PROCEDIMIENTO 'actualizar_prestamos_atrasados' CREADO O VERIFICADO");
    }
}
