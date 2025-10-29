package com.Springboot.Biblioteca_backend.controller;

import com.Springboot.Biblioteca_backend.Entidades.Autor;
import com.Springboot.Biblioteca_backend.repository.AutorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/autor")
public class AutorController {

    private final AutorRepository autorRepository;

    public AutorController(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    // CREATE - agregar autor
    @PostMapping
    public Autor crearAutor(@RequestBody Autor autor) {
        return autorRepository.save(autor);
    }

    // READ - listar todos
    @GetMapping
    public List<Autor> listarAutores() {
        return autorRepository.findAll();
    }

    // READ - buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<Autor> obtenerAutor(@PathVariable Long id) {
        Optional<Autor> autor = autorRepository.findById(id);
        return autor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE - actualizar autor
    @PutMapping("/{id}")
    public ResponseEntity<Autor> actualizarAutor(@PathVariable Long id, @RequestBody Autor autorDetalles) {
        return autorRepository.findById(id)
                .map(autor -> {
                    autor.setNombreCompleto(autorDetalles.getNombreCompleto());
                    autor.setNacionalidad(autorDetalles.getNacionalidad());
                    autor.setFechaNacimiento(autorDetalles.getFechaNacimiento());
                    Autor actualizado = autorRepository.save(autor);
                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE - eliminar autor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAutor(@PathVariable Long id) {
        return autorRepository.findById(id)
                .map(autor -> {
                    autorRepository.delete(autor);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
