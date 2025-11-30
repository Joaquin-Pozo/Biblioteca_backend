package com.Springboot.Biblioteca_backend.controller;

import com.Springboot.Biblioteca_backend.dto.KpiDTO;
import com.Springboot.Biblioteca_backend.service.KpiService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/kpi")
public class KpiController {

    private final KpiService kpiService;

    public KpiController(KpiService kpiService) {
        this.kpiService = kpiService;
    }

    // ✅ READ - obtener KPIs
    @GetMapping
    public KpiDTO obtenerKpis() {
        return kpiService.obtenerKpis();
    }
}
