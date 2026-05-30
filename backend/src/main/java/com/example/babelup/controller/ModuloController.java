package com.example.babelup.controller;

import com.example.babelup.dto.RespostaModuloDto;
import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.service.ModuloService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/modulos")
public class ModuloController {


    private final ModuloService moduloService;


    public ModuloController(ModuloService moduloService){
        this.moduloService = moduloService;
    }

    // GET /api/modulos/listar - Listar todos os módulos
    @GetMapping("/listar")
    public ResponseEntity<Object> listarModulos() {
        try {
            List<Modulo> modulos = moduloService.listarModulos();
            List<RespostaModuloDto> dtos = modulos.stream().map(m-> new RespostaModuloDto(m))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar módulos: " + e.getMessage());
        }
    }

    // GET /api/modulos/{id} - Obter módulo específico
    @GetMapping("/{id}")
    public ResponseEntity<Object> obterModulo(@PathVariable UUID id) {
        try {
            Optional<Modulo> modulo = moduloService.obterModulo(id);
            if (modulo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Módulo não encontrado");
            }
            Modulo m = modulo.get();
            RespostaModuloDto dto = new RespostaModuloDto(m);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter módulo: " + e.getMessage());
        }
    }

    // GET /api/modulos/nivel/{nivelId} - Obter módulos de um nível
        @GetMapping("/nivel/{nivelId}")
    public ResponseEntity<Object> obterModulosPorNivel(@PathVariable UUID nivelId) {
        try {
            List<Modulo> modulos = moduloService.obterModulosPorNivel(nivelId);
            List<RespostaModuloDto> dtos = modulos.stream()
                    .map(m -> new RespostaModuloDto(m))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter módulos do nível: " + e.getMessage());
        }
    }

    // POST /api/modulos - Criar novo módulo


    // DELETE /api/modulos/{id} - Deletar módulo


    // POST /api/modulos/{moduloId}/submeter-exercicio - Registrar conclusão de exercício


    // GET /api/modulos/{moduloId}/progresso/{alunoId} - Obter progresso do aluno



}

