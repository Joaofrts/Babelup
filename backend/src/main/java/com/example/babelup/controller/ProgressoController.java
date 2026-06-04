package com.example.babelup.controller;

import com.example.babelup.dto.RespostaProgressoDto;
import com.example.babelup.entities.progressoGamificacao.ProgressoAluno;
import com.example.babelup.service.ProgressoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/progressos")
public class ProgressoController {

    private final ProgressoService progressoService;

    public ProgressoController(ProgressoService progressoService) {
        this.progressoService = progressoService;
    }

    @GetMapping("/alunos/{alunoId}/modulos/{moduloId}")
    public ResponseEntity<Object> obterProgresso(@PathVariable UUID alunoId, @PathVariable UUID moduloId) {
        ProgressoAluno progresso = progressoService.obterProgresso(alunoId, moduloId);
        if (progresso == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Progresso nÃ£o encontrado");
        }
        return ResponseEntity.ok(new RespostaProgressoDto(progresso));
    }

    @GetMapping("/alunos/{alunoId}/modulos/{moduloId}/concluido")
    public ResponseEntity<Object> moduloFoiConcluido(@PathVariable UUID alunoId, @PathVariable UUID moduloId) {
        return ResponseEntity.ok(Map.of("concluido", progressoService.moduloFoiConcluido(alunoId, moduloId)));
    }
}
