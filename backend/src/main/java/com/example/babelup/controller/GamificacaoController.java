package com.example.babelup.controller;

import com.example.babelup.dto.AdicionarPontosDto;
import com.example.babelup.dto.ApiResponseDtos.RankingResponse;
import com.example.babelup.service.GamificacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gamificacao")
public class GamificacaoController {

    private final GamificacaoService gamificacaoService;

    public GamificacaoController(GamificacaoService gamificacaoService) {
        this.gamificacaoService = gamificacaoService;
    }

    @PostMapping("/pontos")
    public ResponseEntity<Object> adicionarPontos(@RequestBody AdicionarPontosDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new RankingResponse(gamificacaoService.adicionarPontos(dto.alunoId(), dto.pontos())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/ranking")
    public ResponseEntity<Object> listarTop10(@RequestParam int mes, @RequestParam int ano) {
        return ResponseEntity.ok(gamificacaoService.listarTop10DoMes(mes, ano).stream()
                .map(RankingResponse::new)
                .toList());
    }

    @PostMapping("/ranking/processar")
    public ResponseEntity<Object> processarRanking(@RequestParam int mes, @RequestParam int ano) {
        gamificacaoService.processarPosicoesDoMes(mes, ano);
        return ResponseEntity.accepted().build();
    }
}
