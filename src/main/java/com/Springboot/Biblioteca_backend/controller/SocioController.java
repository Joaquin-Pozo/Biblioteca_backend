package com.Springboot.Biblioteca_backend.controller;

import com.Springboot.Biblioteca_backend.Entidades.Socio;
import com.Springboot.Biblioteca_backend.repository.SocioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/socio")
public class SocioController {

    private final SocioRepository socioRepository;

    public SocioController(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    // CREATE - agregar socio
    @PostMapping
    public Socio crearSocio(@RequestBody Socio socio) {
        return socioRepository.save(socio);
    }

    // READ - listar todos
    @GetMapping
    public List<Socio> listarSocios() {
        return socioRepository.findAll();
    }

    // READ - buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<Socio> obtenerSocio(@PathVariable Long id) {
        Optional<Socio> socio = socioRepository.findById(id);
        return socio.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE - actualizar socio
    @PutMapping("/{id}")
    public ResponseEntity<Socio> actualizarSocio(@PathVariable Long id, @RequestBody Socio socioDetalles) {
        return socioRepository.findById(id)
                .map(socio -> {
                    socio.setNombreCompleto(socioDetalles.getNombreCompleto());
                    socio.setCorreo(socioDetalles.getCorreo());
                    socio.setTelefono(socioDetalles.getTelefono());
                    socio.setEstadoSocio(socioDetalles.getEstadoSocio());
                    socio.setFechaInscripcion(socioDetalles.getFechaInscripcion());
                    socio.setIdentificador(socioDetalles.getIdentificador());
                    Socio actualizado = socioRepository.save(socio);
                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE - eliminar socio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSocio(@PathVariable Long id) {
        return socioRepository.findById(id)
                .map(socio -> {
                    socioRepository.delete(socio);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
