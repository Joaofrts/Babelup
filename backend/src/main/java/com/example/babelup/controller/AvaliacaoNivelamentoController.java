package com.example.babelup.controller;

import com.example.babelup.dto.ApiResponseDtos.AvaliacaoResponse;
import com.example.babelup.dto.AvaliacaoNivelamentoDto;
import com.example.babelup.service.AvaliacaoNivelamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/avaliacoes-nivelamento")
public class AvaliacaoNivelamentoController {

    private final AvaliacaoNivelamentoService avaliacaoService;

    public AvaliacaoNivelamentoController(AvaliacaoNivelamentoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @PostMapping
    public ResponseEntity<Object> avaliarAluno(@RequestBody AvaliacaoNivelamentoDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new AvaliacaoResponse(
                    avaliacaoService.avaliarAluno(dto.sessaoId(), dto.alunoId(), dto.nivelRecomendadoId(),
                            dto.tipoTeste(), dto.nota(), dto.parecerProfessor())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @GetMapping("/sessoes/{sessaoId}/alunos/{alunoId}")
    public ResponseEntity<Object> obterAvaliacaoDoAluno(@PathVariable UUID sessaoId, @PathVariable UUID alunoId) {
        try {
            return ResponseEntity.ok(new AvaliacaoResponse(avaliacaoService.obterAvaliacaoDoAluno(sessaoId, alunoId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
