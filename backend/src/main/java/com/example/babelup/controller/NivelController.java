package com.example.babelup.controller;

import com.example.babelup.dto.AdicionarNivelDto;
import com.example.babelup.dto.RespostaNivelDto;
import com.example.babelup.entities.estruturaAcademica.Nivel;
import com.example.babelup.service.NivelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/niveis")
public class NivelController {

    @Autowired
    private NivelService nivelService;

    // GET /api/niveis - Listar todos os níveis
    @GetMapping({"", "/listar"})
    public ResponseEntity<Object> listarNiveis() {
        try {
            List<Nivel> niveis = nivelService.listarNiveis();
            List<RespostaNivelDto> dtos = niveis.stream()
                    .map(n -> new RespostaNivelDto(n))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar níveis: " + e.getMessage());
        }
    }

    // GET /api/niveis/{id} - Obter nível específico
    @GetMapping("/{id}")
    public ResponseEntity<Object> obterNivel(@PathVariable UUID id) {
        try {

            Optional<Nivel> nivel = nivelService.obterNivel(id);
            if (nivel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nível não encontrado");
            }
            Nivel n = nivel.get();
            RespostaNivelDto dto = new RespostaNivelDto(n);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter nível: " + e.getMessage());
        }
    }

    @PostMapping({"", "/criar"})
    public ResponseEntity<Object> criarNivel(@RequestBody AdicionarNivelDto dto) {
        try {
            if (dto.nome() == null || dto.nome().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome do nÃ­vel Ã© obrigatÃ³rio");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(nivelService.criarNivel(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarNivel(@PathVariable UUID id, @RequestBody AdicionarNivelDto dto) {
        try {
            return ResponseEntity.ok(nivelService.atualizarNivel(id, dto.descricao(), dto.cargaHoraria()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarNivel(@PathVariable UUID id) {
        nivelService.deletarNivel(id);
        return ResponseEntity.noContent().build();
    }

}
