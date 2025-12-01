package com.Springboot.Biblioteca_backend.service;

import com.Springboot.Biblioteca_backend.Entidades.Autor;
import com.Springboot.Biblioteca_backend.dto.EtlSociosResultDto;
import com.Springboot.Biblioteca_backend.repository.AutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class AutorEtlService {

    private final AutorRepository autorRepository;

    public AutorEtlService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public EtlSociosResultDto procesarCsv(MultipartFile file) {
        EtlSociosResultDto resultado = new EtlSociosResultDto();

        // ✅ Validación básica de archivo
        if (file == null || file.isEmpty()) {
            resultado.agregarError(0, "No se recibió archivo o está vacío.");
            return resultado;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String linea;
            int numeroFila = 0;

            // ✅ Leer cabecera
            linea = reader.readLine();
            if (linea == null) {
                resultado.agregarError(0, "El archivo CSV está vacío.");
                return resultado;
            }
            numeroFila++; // fila 1 = cabecera

            // ✅ Esperamos: nombre_completo,nacionalidad,fecha_nacimiento
            String[] header = linea.split(",");
            if (header.length < 3) {
                resultado.agregarError(1, "La cabecera del CSV no tiene las 3 columnas esperadas.");
                return resultado;
            }

            // ✅ Procesar filas
            while ((linea = reader.readLine()) != null) {
                numeroFila++;

                if (linea.trim().isEmpty()) {
                    continue;
                }

                resultado.setTotalFilas(resultado.getTotalFilas() + 1);

                String[] columnas = linea.split(",", -1);

                if (columnas.length < 3) {
                    resultado.incrementarErrores();
                    resultado.agregarError(numeroFila, "La fila tiene menos columnas de las esperadas.");
                    continue;
                }

                String nombreCompleto = columnas[0].trim();
                String nacionalidad = columnas[1].trim();
                String fechaNacimientoStr = columnas[2].trim();

                // ---------- VALIDACIONES ----------

                if (nombreCompleto.isEmpty()) {
                    resultado.incrementarErrores();
                    resultado.agregarError(numeroFila, "El nombre_completo es obligatorio.");
                    continue;
                }

                if (nombreCompleto.length() > 200) {
                    resultado.incrementarErrores();
                    resultado.agregarError(numeroFila, "El nombre_completo excede los 200 caracteres.");
                    continue;
                }

                if (!nacionalidad.isEmpty() && nacionalidad.length() > 60) {
                    resultado.incrementarErrores();
                    resultado.agregarError(numeroFila, "La nacionalidad excede los 60 caracteres.");
                    continue;
                }

                LocalDate fechaNacimiento = null;
                if (!fechaNacimientoStr.isEmpty()) {
                    try {
                        fechaNacimiento = LocalDate.parse(fechaNacimientoStr);
                    } catch (DateTimeParseException e) {
                        resultado.incrementarErrores();
                        resultado.agregarError(
                                numeroFila,
                                "fecha_nacimiento inválida. Formato esperado: yyyy-MM-dd."
                        );
                        continue;
                    }
                }

                // ---------- ✅ INSERT O UPDATE (UPSERT) ----------

                List<Autor> autoresExistentes =
                        autorRepository.findByNombreCompletoIgnoreCase(nombreCompleto);

                if (!autoresExistentes.isEmpty()) {
                    // ✅ ACTUALIZA SOLO EL PRIMER AUTOR ENCONTRADO
                    Autor autor = autoresExistentes.get(0);
                    autor.setNacionalidad(nacionalidad.isEmpty() ? null : nacionalidad);
                    autor.setFechaNacimiento(fechaNacimiento);
                    autorRepository.save(autor);
                    resultado.incrementarActualizadas();

                } else {
                    // ✅ INSERT
                    Autor autor = new Autor();
                    autor.setNombreCompleto(nombreCompleto);
                    autor.setNacionalidad(nacionalidad.isEmpty() ? null : nacionalidad);
                    autor.setFechaNacimiento(fechaNacimiento);
                    autorRepository.save(autor);
                    resultado.incrementarInsertadas();
                }
            }

        } catch (Exception e) {
            resultado.agregarError(0, "Error procesando el archivo: " + e.getMessage());
        }

        return resultado;
    }
}
