package com.Springboot.Biblioteca_backend.service;

import com.Springboot.Biblioteca_backend.Entidades.EstadoSocio;
import com.Springboot.Biblioteca_backend.Entidades.Socio;
import com.Springboot.Biblioteca_backend.dto.EtlSociosResultDto;
import com.Springboot.Biblioteca_backend.repository.SocioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Optional;

@Service
public class SocioEtlService {

    private final SocioRepository socioRepository;

    public SocioEtlService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    public EtlSociosResultDto procesarCsv(MultipartFile file) {
        EtlSociosResultDto resultado = new EtlSociosResultDto();

        if (file == null || file.isEmpty()) {
            resultado.agregarError(0, "No se recibió archivo o está vacío.");
            return resultado;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String linea;
            int numeroFila = 0;

            // Leer cabecera
            linea = reader.readLine();
            if (linea == null) {
                resultado.agregarError(0, "El archivo CSV está vacío.");
                return resultado;
            }
            numeroFila++; // fila 1 = cabecera

            // Esperamos: identificador,nombre_completo,correo,telefono,estado_socio,fecha_inscripcion
            String[] header = linea.split(",");
            if (header.length < 6) {
                resultado.agregarError(1, "La cabecera del CSV no tiene las 6 columnas esperadas.");
                return resultado;
            }

            // Procesar filas de datos
            while ((linea = reader.readLine()) != null) {
                numeroFila++; // empieza en 2 para la primera fila de datos

                // Saltar filas totalmente vacías
                if (linea.trim().isEmpty()) {
                    continue;
                }

                resultado.setTotalFilas(resultado.getTotalFilas() + 1);

                String[] columnas = linea.split(",", -1); // -1 para incluir vacíos

                if (columnas.length < 6) {
                    resultado.incrementarErrores();
                    resultado.agregarError(numeroFila, "La fila tiene menos columnas de las esperadas.");
                    continue;
                }

                String identificador = columnas[0].trim();
                String nombreCompleto = columnas[1].trim();
                String correo = columnas[2].trim();
                String telefono = columnas[3].trim();
                String estadoSocioStr = columnas[4].trim();
                String fechaInscripcionStr = columnas[5].trim();

                // ---------- VALIDACIONES ----------

                // identificador (obligatorio)
                if (identificador.isEmpty()) {
                    resultado.incrementarErrores();
                    resultado.agregarError(numeroFila, "El identificador es obligatorio.");
                    continue;
                }
                if (identificador.length() > 12) {
                    resultado.incrementarErrores();
                    resultado.agregarError(numeroFila, "El identificador excede los 12 caracteres.");
                    continue;
                }

                // nombre_completo (obligatorio)
                if (nombreCompleto.isEmpty()) {
                    resultado.incrementarErrores();
                    resultado.agregarError(numeroFila, "El nombre_completo es obligatorio.");
                    continue;
                }

                // correo (obligatorio, formato simple)
                if (correo.isEmpty()) {
                    resultado.incrementarErrores();
                    resultado.agregarError(numeroFila, "El correo es obligatorio.");
                    continue;
                }
                correo = correo.toLowerCase(Locale.ROOT);
                if (!correo.contains("@")) {
                    resultado.incrementarErrores();
                    resultado.agregarError(numeroFila, "El correo es inválido (no contiene '@').");
                    continue;
                }

                // estado_socio (obligatorio, mapea al enum EstadoSocio)
                if (estadoSocioStr.isEmpty()) {
                    resultado.incrementarErrores();
                    resultado.agregarError(numeroFila, "El estado_socio es obligatorio.");
                    continue;
                }

                EstadoSocio estadoSocio;
                try {
                    // Enum definido como: disponible, restringido, suspendido (en minúsculas)
                    estadoSocio = EstadoSocio.valueOf(estadoSocioStr.toLowerCase(Locale.ROOT));
                } catch (IllegalArgumentException e) {
                    resultado.incrementarErrores();
                    resultado.agregarError(
                            numeroFila,
                            "estado_socio inválido: '" + estadoSocioStr +
                                    "'. Valores permitidos: disponible, restringido, suspendido."
                    );
                    continue;
                }

                // fecha_inscripcion (opcional en CSV, NOT NULL en BD)
                LocalDate fechaInscripcion;
                if (fechaInscripcionStr.isEmpty()) {
                    fechaInscripcion = LocalDate.now();
                } else {
                    try {
                        fechaInscripcion = LocalDate.parse(fechaInscripcionStr);
                    } catch (DateTimeParseException e) {
                        resultado.incrementarErrores();
                        resultado.agregarError(
                                numeroFila,
                                "fecha_inscripcion inválida. Formato esperado: yyyy-MM-dd."
                        );
                        continue;
                    }
                }

                // ---------- VALIDACIÓN UNIQUE DE CORREO ----------
                Optional<Socio> socioConCorreo = socioRepository.findByCorreo(correo);
                Optional<Socio> socioExistente = socioRepository.findByIdentificador(identificador);

                if (socioConCorreo.isPresent()) {
                    Socio sCorreo = socioConCorreo.get();
                    // si hay socio con este correo pero distinto identificador -> error
                    if (socioExistente.isEmpty() || !sCorreo.getId().equals(socioExistente.get().getId())) {
                        resultado.incrementarErrores();
                        resultado.agregarError(
                                numeroFila,
                                "El correo '" + correo + "' ya está siendo utilizado por otro socio."
                        );
                        continue;
                    }
                }

                // ---------- INSERT o UPDATE ----------
                Socio socio;
                if (socioExistente.isPresent()) {
                    // UPDATE
                    socio = socioExistente.get();
                    socio.setNombreCompleto(nombreCompleto);
                    socio.setCorreo(correo);
                    socio.setTelefono(telefono.isEmpty() ? null : telefono);
                    socio.setEstadoSocio(estadoSocio);
                    socio.setFechaInscripcion(fechaInscripcion);
                    socioRepository.save(socio);
                    resultado.incrementarActualizadas();
                } else {
                    // INSERT
                    socio = new Socio();
                    socio.setIdentificador(identificador);
                    socio.setNombreCompleto(nombreCompleto);
                    socio.setCorreo(correo);
                    socio.setTelefono(telefono.isEmpty() ? null : telefono);
                    socio.setEstadoSocio(estadoSocio);
                    socio.setFechaInscripcion(fechaInscripcion);
                    socioRepository.save(socio);
                    resultado.incrementarInsertadas();
                }
            }

        } catch (Exception e) {
            resultado.agregarError(0, "Error procesando el archivo: " + e.getMessage());
        }

        return resultado;
    }
}

