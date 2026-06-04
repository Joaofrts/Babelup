package com.example.babelup.controller;

import com.example.babelup.dto.ApiResponseDtos.RespostaAlunoResponse;
import com.example.babelup.dto.SubmeterRespostaDto;
import com.example.babelup.service.PraticaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/praticas")
public class PraticaController {

    private final PraticaService praticaService;

    public PraticaController(PraticaService praticaService) {
        this.praticaService = praticaService;
    }

    @PostMapping("/respostas")
    public ResponseEntity<Object> submeterResposta(@RequestBody SubmeterRespostaDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new RespostaAlunoResponse(
                    praticaService.submeterResposta(dto.alunoId(), dto.exercicioId(), dto.resposta())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @GetMapping("/alunos/{alunoId}/exercicios/{exercicioId}/historico")
    public ResponseEntity<Object> listarHistorico(@PathVariable UUID alunoId, @PathVariable UUID exercicioId) {
        return ResponseEntity.ok(praticaService.listarHistoricoDeRespostas(alunoId, exercicioId).stream()
                .map(RespostaAlunoResponse::new)
                .toList());
    }
}
