package com.Springboot.Biblioteca_backend.controller;

import com.Springboot.Biblioteca_backend.Entidades.Libro;
import com.Springboot.Biblioteca_backend.repository.LibroRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/libro")
public class LibroController {

    private final LibroRepository libroRepository;

    public LibroController(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    // CREATE - agregar libro
    @PostMapping
    public Libro crearLibro(@RequestBody Libro libro) {
        return libroRepository.save(libro);
    }

    // READ - listar todos
    @GetMapping
    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }

    // READ - buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerLibro(@PathVariable Long id) {
        Optional<Libro> libro = libroRepository.findById(id);
        return libro.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE - actualizar libro
    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable Long id, @RequestBody Libro libroDetalles) {
        return libroRepository.findById(id)
                .map(libro -> {
                    libro.setTitulo(libroDetalles.getTitulo());
                    libro.setCategoria(libroDetalles.getCategoria());
                    libro.setEditorial(libroDetalles.getEditorial());
                    libro.setAnioPublicacion(libroDetalles.getAnioPublicacion());
                    libro.setStock(libroDetalles.getStock());
                    libro.setAutor(libroDetalles.getAutor()); // suponiendo que tienes relación con Autor
                    Libro actualizado = libroRepository.save(libro);
                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE - eliminar libro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        return libroRepository.findById(id)
                .map(libro -> {
                    libroRepository.delete(libro);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
