package com.Springboot.Biblioteca_backend.controller;

import com.Springboot.Biblioteca_backend.dto.EtlSociosResultDto;
import com.Springboot.Biblioteca_backend.service.AutorEtlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
@RequestMapping("/autor/etl")
public class AutorEtlController {

    private final AutorEtlService autorEtlService;

    public AutorEtlController(AutorEtlService autorEtlService) {
        this.autorEtlService = autorEtlService;
    }

    @PostMapping("/upload")
    public ResponseEntity<EtlSociosResultDto> uploadAutoresCsv(@RequestParam("file") MultipartFile file) {

        if (file == null || file.isEmpty()) {
            EtlSociosResultDto errorResult = new EtlSociosResultDto();
            errorResult.agregarError(0, "No se recibió archivo o está vacío.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
        }

        EtlSociosResultDto resultado = autorEtlService.procesarCsv(file);
        return ResponseEntity.ok(resultado);
    }
}
