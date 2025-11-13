package com.Springboot.Biblioteca_backend.controller;

import com.Springboot.Biblioteca_backend.Entidades.*;
import com.Springboot.Biblioteca_backend.repository.CopiaRepository;
import com.Springboot.Biblioteca_backend.repository.PrestamoRepository;
import com.Springboot.Biblioteca_backend.repository.SocioRepository;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/prestamo")
public class PrestamoController {

    private final PrestamoRepository prestamoRepository;
    private final CopiaRepository copiaRepository;
    private final SocioRepository socioRepository;

    public PrestamoController(
            PrestamoRepository prestamoRepository,
            CopiaRepository copiaRepository,
            SocioRepository socioRepository
    ) {
        this.prestamoRepository = prestamoRepository;
        this.copiaRepository = copiaRepository;
        this.socioRepository = socioRepository;
    }

    // CREAR PRÉSTAMO
    @PostMapping
    public ResponseEntity<?> registrarPrestamo(@RequestBody Prestamo prestamo) {
        // Verificar copia
        Optional<Copia> copiaOpt = copiaRepository.findById(prestamo.getCopia().getId());
        if (copiaOpt.isEmpty()) {
            return ResponseEntity.ok().body("La copia no existe.");
        }

        Copia copia = copiaOpt.get();
        if (copia.getEstadoCopia() != EstadoCopia.disponible) {
            return ResponseEntity.ok().body("La copia no está disponible para préstamo.");
        }

        // Verificar socio
        Optional<Socio> socioOpt = socioRepository.findById(prestamo.getSocio().getId());
        if (socioOpt.isEmpty()) {
            return ResponseEntity.ok().body("El socio no existe.");
        }

        Socio socio = socioOpt.get();
        if (socio.getEstadoSocio() != EstadoSocio.disponible) {
            return ResponseEntity.ok().body("El socio está restringido o suspendido y no puede realizar préstamos.");
        }

        //  Validar fecha de devolución pactada
        if (prestamo.getFechaPactadaDevolucion() == null) {
            return ResponseEntity.ok().body("Debe indicar la fecha pactada de devolución.");
        }

        LocalDate hoy = LocalDate.now();
        LocalDate maxPermitido = hoy.plusDays(14);
        LocalDate pactada = prestamo.getFechaPactadaDevolucion();

        if (pactada.isBefore(hoy)) {
            return ResponseEntity.ok().body("La fecha pactada debe ser igual o posterior a la fecha actual.");
        }

        if (pactada.isAfter(maxPermitido)) {
            return ResponseEntity.ok().body("La fecha pactada no puede superar los 14 días desde hoy.");
        }

        // Configurar datos del préstamo
        prestamo.setFechaPrestamo(hoy);
        prestamo.setEstadoPrestamo(EstadoPrestamo.activo);
        prestamo.setMulta(BigDecimal.ZERO);
        prestamo.setDaniado(false);

        // Guardar préstamo
        Prestamo guardado = prestamoRepository.save(prestamo);

        // Cambiar estado de la copia
        copia.setEstadoCopia(EstadoCopia.prestado);
        copiaRepository.save(copia);

        return ResponseEntity.ok(guardado);
    }

    // REGISTRAR DEVOLUCIÓN
    @PutMapping("/devolver/{id}")
    public ResponseEntity<?> registrarDevolucion(@PathVariable Long id, @RequestBody(required = false) Prestamo devolucionDetalles) {
        Optional<Prestamo> prestamoOpt = prestamoRepository.findById(id);
        if (prestamoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Prestamo prestamo = prestamoOpt.get();
        if (prestamo.getEstadoPrestamo() != EstadoPrestamo.activo) {
            return ResponseEntity.badRequest().body("El préstamo ya fue devuelto o está inactivo.");
        }

        prestamo.setFechaDevolucion(LocalDate.now());
        prestamo.setEstadoPrestamo(EstadoPrestamo.completado);

        Socio socio = prestamo.getSocio();
        boolean restringir = false;

        // Verificar atraso
        if (prestamo.getFechaDevolucion().isAfter(prestamo.getFechaPactadaDevolucion())) {
            long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(
                    prestamo.getFechaPactadaDevolucion(),
                    prestamo.getFechaDevolucion()
            );

            prestamo.setEstadoPrestamo(EstadoPrestamo.atrasado);
            BigDecimal multa = BigDecimal.valueOf(diasAtraso * 500);
            prestamo.setMulta(multa);
            restringir = true;
        }

        // Verificar daño
        Copia copia = prestamo.getCopia();
        if (devolucionDetalles != null && Boolean.TRUE.equals(devolucionDetalles.getDaniado())) {
            prestamo.setDaniado(true);
            prestamo.setMulta(prestamo.getMulta().add(BigDecimal.valueOf(2000)));
            copia.setEstadoCopia(EstadoCopia.reparacion);
            restringir = true;
        } else {
            copia.setEstadoCopia(EstadoCopia.disponible);
        }

        // Si hubo atraso o daño → socio restringido
        if (restringir) {
            socio.setEstadoSocio(EstadoSocio.restringido);
            socioRepository.save(socio);
        }

        prestamoRepository.save(prestamo);
        copiaRepository.save(copia);

        return ResponseEntity.ok(prestamo);
    }

    // LISTAR TODOS LOS PRÉSTAMOS
    @GetMapping
    public List<Prestamo> listarPrestamos() {
        return prestamoRepository.findAll();
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> obtenerPrestamo(@PathVariable Long id) {
        return prestamoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
