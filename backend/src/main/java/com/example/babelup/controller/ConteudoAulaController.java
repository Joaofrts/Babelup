package com.example.babelup.controller;

import com.example.babelup.dto.AdicionarExercicioDto;
import com.example.babelup.dto.AdicionarMaterialApoioDto;
import com.example.babelup.dto.AdicionarVideoAulaDto;
import com.example.babelup.dto.ApiResponseDtos.ExercicioResponse;
import com.example.babelup.dto.ApiResponseDtos.MaterialApoioResponse;
import com.example.babelup.dto.ApiResponseDtos.VideoAulaResponse;
import com.example.babelup.service.ConteudoAulaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/conteudos")
public class ConteudoAulaController {

    private final ConteudoAulaService conteudoAulaService;

    public ConteudoAulaController(ConteudoAulaService conteudoAulaService) {
        this.conteudoAulaService = conteudoAulaService;
    }

    @PostMapping("/videoaulas")
    public ResponseEntity<Object> adicionarVideoAula(@RequestBody AdicionarVideoAulaDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new VideoAulaResponse(
                    conteudoAulaService.adicionarVideoAula(dto.moduloId(), dto.titulo(), dto.url(), dto.duracao(),
                            dto.tipo(), dto.dataTransmissao())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/modulos/{moduloId}/videoaulas")
    public ResponseEntity<Object> listarVideoAulasPorModulo(@PathVariable UUID moduloId) {
        return ResponseEntity.ok(conteudoAulaService.listarAulasPorModulo(moduloId).stream()
                .map(VideoAulaResponse::new)
                .toList());
    }

    @PostMapping("/materiais")
    public ResponseEntity<Object> adicionarMaterialApoio(@RequestBody AdicionarMaterialApoioDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new MaterialApoioResponse(
                    conteudoAulaService.adicionarMaterialApoio(dto.moduloId(), dto.titulo(), dto.urlPdf())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/modulos/{moduloId}/materiais")
    public ResponseEntity<Object> listarMateriaisPorModulo(@PathVariable UUID moduloId) {
        return ResponseEntity.ok(conteudoAulaService.listarMateriaisPorModulo(moduloId).stream()
                .map(MaterialApoioResponse::new)
                .toList());
    }

    @PostMapping("/exercicios")
    public ResponseEntity<Object> adicionarExercicio(@RequestBody AdicionarExercicioDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ExercicioResponse(
                    conteudoAulaService.adicionarExercicioAula(dto.videoAulaId(), dto.enunciado(),
                            dto.alternativas(), dto.respostaCorreta())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/videoaulas/{videoAulaId}/exercicios")
    public ResponseEntity<Object> listarExerciciosPorAula(@PathVariable UUID videoAulaId) {
        return ResponseEntity.ok(conteudoAulaService.listarExerciciosPorAula(videoAulaId).stream()
                .map(ExercicioResponse::new)
                .toList());
    }
}
