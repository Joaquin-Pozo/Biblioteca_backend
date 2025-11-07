package com.Springboot.Biblioteca_backend.controller;

import com.Springboot.Biblioteca_backend.Entidades.Copia;
import com.Springboot.Biblioteca_backend.Entidades.EstadoCopia;
import com.Springboot.Biblioteca_backend.Entidades.Libro;
import com.Springboot.Biblioteca_backend.repository.CopiaRepository;
import com.Springboot.Biblioteca_backend.repository.LibroRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/libro")
public class LibroController {

    private final LibroRepository libroRepository;
    private final CopiaRepository copiaRepository;

    public LibroController(LibroRepository libroRepository, CopiaRepository copiaRepository) {
        this.libroRepository = libroRepository;
        this.copiaRepository = copiaRepository;
    }

    // CREATE - agregar libro
    // @PostMapping
    // public Libro crearLibro(@RequestBody Libro libro) {
    //    return libroRepository.save(libro);
    // }

    @PostMapping
    public Libro crearLibro(@RequestBody Libro libro) {
        // Guardamos el libro primero
        Libro libroGuardado = libroRepository.save(libro);
        int cantidad = (libro.getCantidad() == null || libro.getCantidad() < 1) ? 1 : libro.getCantidad();

        // Si tiene cantidad definida, creamos las copias
        for (int i = 1; i <= cantidad; i++) {
            Copia copia = new Copia();
            copia.setLibro(libroGuardado);
            copia.setCodigoBarras("LIB-" + libroGuardado.getId() + "-CP-" + i); // ejemplo: LIB-1-CP-3
            copia.setFechaAdquisicion(LocalDate.now());
            copia.setEstadoCopia(EstadoCopia.disponible);
            copiaRepository.save(copia);
        }
        return libroGuardado;
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

    @GetMapping("/buscar")
    public List<Libro> buscarPorTitulo(@RequestParam("titulo") String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    @GetMapping("/buscarPorAutor")
    public List<Libro> buscarPorAutor(@RequestParam("autor") String autor) {
        return libroRepository.findByAutorNombreCompletoContainingIgnoreCase(autor);
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
