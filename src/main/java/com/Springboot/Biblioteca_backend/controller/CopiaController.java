package com.Springboot.Biblioteca_backend.controller;

import com.Springboot.Biblioteca_backend.Entidades.Copia;
import com.Springboot.Biblioteca_backend.Entidades.EstadoCopia;
import com.Springboot.Biblioteca_backend.repository.CopiaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/copia")
public class CopiaController {

    private final CopiaRepository copiaRepository;

    public CopiaController(CopiaRepository copiaRepository) {
        this.copiaRepository = copiaRepository;
    }

    // CREATE
    @PostMapping
    public Copia crearCopia(@RequestBody Copia copia) {
        return copiaRepository.save(copia);
    }

    // READ - listar todas
    @GetMapping
    public List<Copia> listarCopias() {
        return copiaRepository.findAll();
    }

    // READ - buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<Copia> obtenerCopia(@PathVariable Long id) {
        Optional<Copia> copia = copiaRepository.findById(id);
        return copia.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Stock disponible por id_libro
    @GetMapping("/disponibles/{idLibro}")
    public long contarDisponibles(@PathVariable Long idLibro) {
        return copiaRepository.countByLibroIdAndEstadoCopia(idLibro, EstadoCopia.disponible);
    }

    // Cantidad de copias por id_libro
    @GetMapping("/stock/{idLibro}")
    public long contarStock(@PathVariable Long idLibro) {
        return copiaRepository.countByLibroId(idLibro);
    }

    // Cambia el estado de una copia en reparacion a disponible
    @PutMapping("/reparar/{id}")
    public ResponseEntity<?> repararCopia(@PathVariable Long id) {
        Optional<Copia> copiaOpt = copiaRepository.findById(id);
        if (copiaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Copia copia = copiaOpt.get();

        if (copia.getEstadoCopia() != EstadoCopia.reparacion) {
            return ResponseEntity.badRequest().body("La copia no está en reparación.");
        }
        // Cambiar estado a disponible
        copia.setEstadoCopia(EstadoCopia.disponible);
        copiaRepository.save(copia);

        return ResponseEntity.ok("La copia ha sido reparada y ahora está disponible para préstamo.");
    }


    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Copia> actualizarCopia(@PathVariable Long id, @RequestBody Copia copiaDetalles) {
        return copiaRepository.findById(id)
                .map(copia -> {
                    copia.setLibro(copiaDetalles.getLibro());
                    copia.setCodigoBarras(copiaDetalles.getCodigoBarras());
                    copia.setFechaAdquisicion(copiaDetalles.getFechaAdquisicion());
                    copia.setEstadoCopia(copiaDetalles.getEstadoCopia());
                    Copia actualizada = copiaRepository.save(copia);
                    return ResponseEntity.ok(actualizada);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCopia(@PathVariable Long id) {
        return copiaRepository.findById(id)
                .map(copia -> {
                    copiaRepository.delete(copia);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
