package com.Springboot.Biblioteca_backend.service;

import com.Springboot.Biblioteca_backend.dto.KpiDTO;
import com.Springboot.Biblioteca_backend.repository.CopiaRepository;
import com.Springboot.Biblioteca_backend.repository.LibroRepository;
import com.Springboot.Biblioteca_backend.repository.PrestamoRepository;
import com.Springboot.Biblioteca_backend.repository.SocioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class KpiService {

    private final PrestamoRepository prestamoRepository;
    private final CopiaRepository copiaRepository;
    private final LibroRepository libroRepository;
    private final SocioRepository socioRepository;

    public KpiService(
            PrestamoRepository prestamoRepository,
            CopiaRepository copiaRepository,
            LibroRepository libroRepository,
            SocioRepository socioRepository
    ) {
        this.prestamoRepository = prestamoRepository;
        this.copiaRepository = copiaRepository;
        this.libroRepository = libroRepository;
        this.socioRepository = socioRepository;
    }

    public KpiDTO obtenerKpis() {

        KpiDTO resultado = new KpiDTO();

        LocalDate ultimaFechaPrestamo = libroRepository.obtenerUltimaFechaPrestamo();

        // KPI 1: Tiempo máximo de un libro sin registrar préstamos
        long diasSinPrestamo = 0;
        if (ultimaFechaPrestamo != null) {
            diasSinPrestamo = ChronoUnit.DAYS.between(ultimaFechaPrestamo, LocalDate.now());
        }

        // KPI 1.1: % Socios activos
        Long totalSocios = socioRepository.contarTotalSocios();
        Long sociosActivos = socioRepository.contarSociosActivos();

        double porcentajeSociosActivos = 0;
        if (totalSocios > 0) {
            porcentajeSociosActivos = (sociosActivos * 100.0) / totalSocios;
        }

        // KPI 2: Préstamos activos
        Long prestamosActivos = prestamoRepository.contarPrestamosActivos();

        // KPI 3: % Préstamos atrasados
        Long prestamosAtrasados = prestamoRepository.contarPrestamosAtrasados();
        Long totalPrestamos = prestamoRepository.count();

        double porcentajeAtrasos = 0;
        if (totalPrestamos > 0) {
            porcentajeAtrasos = (prestamosAtrasados * 100.0) / totalPrestamos;
        }

        // KPI 4: % Libros prestados
        Long copiasPrestadas = copiaRepository.contarCopiasPrestadas();
        Long totalCopias = copiaRepository.count();

        double porcentajeLibrosPrestados = 0;
        if (totalCopias > 0) {
            porcentajeLibrosPrestados = (copiasPrestadas * 100.0) / totalCopias;
        }

        // Setear resultados
        resultado.setPorcentajeSociosActivos(porcentajeSociosActivos);
        resultado.setDiasSinPrestamo(diasSinPrestamo);
        resultado.setPorcentajeAtrasos(porcentajeAtrasos);
        resultado.setPorcentajeLibrosPrestados(porcentajeLibrosPrestados);
        resultado.setPrestamosActivos(prestamosActivos);

        return resultado;
    }
}
