package com.example.babelup.controller;

import com.example.babelup.dto.NivelDto;
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
    @GetMapping("/listar")
    public ResponseEntity<Object> listarNiveis() {
        try {
            List<Nivel> niveis = nivelService.listarNiveis();
            List<NivelDto> dtos = niveis.stream()
                    .map(n -> new NivelDto(n.getId(), n.getNome(), n.getCargaHoraria()))
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
            NivelDto dto = new NivelDto(n.getId(), n.getNome(), n.getCargaHoraria());
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter nível: " + e.getMessage());
        }
    }

    // POST /api/niveis - Criar novo nível
    @PostMapping("/criar")
    public ResponseEntity<Object> criarNivel(@RequestBody NivelDto dto) {
        try {
            if (dto.getNome() == null || dto.getNome().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome do nível é obrigatório");
            }
            
            Nivel nivel = nivelService.criarNivel(dto.getNome(), dto.getCargaHorariaEstimada());
            NivelDto resposta = new NivelDto(nivel.getId(), nivel.getNome(), nivel.getCargaHoraria());
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar nível: " + e.getMessage());
        }
    }

    // PUT /api/niveis/{id} - Atualizar nível
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarNivel(@PathVariable UUID id, @RequestBody NivelDto dto) {
        try {
            Nivel nivel = nivelService.atualizarNivel(id, dto.getNome(), dto.getCargaHorariaEstimada());
            NivelDto resposta = new NivelDto(nivel.getId(), nivel.getNome(), nivel.getCargaHoraria());
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar nível: " + e.getMessage());
        }
    }

    // DELETE /api/niveis/{id} - Deletar nível
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarNivel(@PathVariable UUID id) {
        try {
            nivelService.deletarNivel(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar nível: " + e.getMessage());
        }
    }

    // GET /api/niveis/{id}/pode-progredir/{alunoId} - Validar se aluno pode progredir
    @GetMapping("/{nivelId}/pode-progredir/{alunoId}")
    public ResponseEntity<Object> podeProgredirParaProximo(@PathVariable UUID nivelId, @PathVariable UUID alunoId) {
        try {
            boolean pode = nivelService.podeProgressarParaProximo(alunoId, nivelId);
            return ResponseEntity.ok().body(new Object() {
                public final boolean pode_progredir = pode;
            });
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao validar progressão: " + e.getMessage());
        }
    }

    // GET /api/niveis/{id}/proximo - Obter próximo nível
    @GetMapping("/{id}/proximo")
    public ResponseEntity<Object> obterProximoNivel(@PathVariable UUID id) {
        try {
            Optional<Nivel> proximo = nivelService.obterProximoNivel(id);
            if (proximo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não há próximo nível");
            }
            Nivel n = proximo.get();
            NivelDto dto = new NivelDto(n.getId(), n.getNome(), n.getCargaHoraria());
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter próximo nível: " + e.getMessage());
        }
    }
}
