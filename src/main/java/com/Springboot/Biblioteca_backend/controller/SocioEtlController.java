package com.Springboot.Biblioteca_backend.controller;

import com.Springboot.Biblioteca_backend.dto.EtlSociosResultDto;
import com.Springboot.Biblioteca_backend.service.SocioEtlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
@RequestMapping("/socio/etl")
public class SocioEtlController {

    private final SocioEtlService socioEtlService;

    public SocioEtlController(SocioEtlService socioEtlService) {
        this.socioEtlService = socioEtlService;
    }

    @PostMapping("/upload")
    public ResponseEntity<EtlSociosResultDto> uploadSociosCsv(@RequestParam("file") MultipartFile file) {

        if (file == null || file.isEmpty()) {
            EtlSociosResultDto errorResult = new EtlSociosResultDto();
            errorResult.agregarError(0, "No se recibió archivo o está vacío.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
        }

        EtlSociosResultDto resultado = socioEtlService.procesarCsv(file);
        return ResponseEntity.ok(resultado);
    }
}

