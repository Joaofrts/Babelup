package com.example.babelup.controller;

import com.example.babelup.dto.RespostaModuloDto;
import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.factory.AdminCreationStrategy;
import com.example.babelup.service.ModuloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ModuloController.class);


    public ModuloController(ModuloService moduloService){
        this.moduloService = moduloService;
    }

    // GET /api/modulos/listar - Listar todos os módulos
    @GetMapping("/listar")
    public ResponseEntity<Object> listarModulos() {
        try {
            List<Modulo> modulos = moduloService.listarModulos();
            List<RespostaModuloDto> dtos = modulos.stream().map(RespostaModuloDto::new)
                    .toList();
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar módulos: " + e.getMessage());
        }
    }

    // GET /api/modulos/{id} - Obter módulo específico
    @GetMapping("/{id}")
    public ResponseEntity<Object> obterModulo(@PathVariable String id) {
        try {
            Optional<Modulo> modulo = moduloService.obterModulo(UUID.fromString(id));
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
    public ResponseEntity<Object> obterModulosPorNivel(@PathVariable String nivelId) {
        try {
            List<Modulo> modulos = moduloService.obterModulosPorNivel(UUID.fromString(nivelId));
            List<RespostaModuloDto> dtos = modulos.stream()
                    .map(RespostaModuloDto::new)
                    .toList();
            return ResponseEntity.ok(dtos);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("ID de nível inválido: " + e.getMessage());
        }catch (Exception e) {
            logger.warn("⚠️  ERRO AO OBTER MÓDULOS POR NÍVEL: nivelId={}, erro={}", nivelId, e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter módulos do nível: " + e.getMessage());
        }
    }



}

