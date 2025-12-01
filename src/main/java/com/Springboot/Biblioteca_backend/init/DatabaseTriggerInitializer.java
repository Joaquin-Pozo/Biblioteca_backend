package com.Springboot.Biblioteca_backend.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseTriggerInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseTriggerInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {

        // FUNCIÓN DEL TRIGGER
        String functionSql = """
            CREATE OR REPLACE FUNCTION trg_restringir_socio_por_atraso()
            RETURNS TRIGGER
            LANGUAGE plpgsql
            AS $$
            BEGIN
                IF NEW.estado_prestamo = 'atrasado' THEN
                    UPDATE socio
                    SET estado_socio = 'restringido'
                    WHERE id = NEW.id_socio;
                END IF;

                RETURN NEW;
            END;
            $$;
        """;

        // ELIMINAR TRIGGER SI YA EXISTE (EVITA ERROR)
        String dropTriggerSql = """
            DROP TRIGGER IF EXISTS tg_restringir_socio ON prestamo;
        """;

        // CREAR TRIGGER
        String triggerSql = """
            CREATE TRIGGER tg_restringir_socio
            AFTER UPDATE ON prestamo
            FOR EACH ROW
            EXECUTE FUNCTION trg_restringir_socio_por_atraso();
        """;

        try {
            jdbcTemplate.execute(functionSql);
            jdbcTemplate.execute(dropTriggerSql);
            jdbcTemplate.execute(triggerSql);

            System.out.println(" TRIGGER 'tg_restringir_socio' CREADO O VERIFICADO CORRECTAMENTE");

        } catch (Exception e) {
            System.err.println(" ERROR al crear trigger: " + e.getMessage());
        }
    }
}
