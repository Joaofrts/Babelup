package com.example.babelup.controller;

import com.example.babelup.dto.CursoCatalogoDTO;
import com.example.babelup.service.CatalogoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {
    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping({"", "/cursos"})
    public ResponseEntity<List<CursoCatalogoDTO>> listarCursosDisponiveis() {
        List<CursoCatalogoDTO> cursos = catalogoService.listarCursosParaVitrine();
        return ResponseEntity.ok(cursos);
    }
}
