package com.example.babelup.controller;

import com.example.babelup.dto.AbrirSessaoDto;
import com.example.babelup.dto.ApiResponseDtos.SessaoConversacaoResponse;
import com.example.babelup.dto.FinalizarSessaoDto;
import com.example.babelup.service.SessaoConversacaoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping({"/api/agendamentos"})
public class AgendamentoController {

    private final SessaoConversacaoService sessaoService;

    public AgendamentoController(SessaoConversacaoService sessaoService) {
        this.sessaoService = sessaoService;
    }

    @GetMapping
    public ResponseEntity<Object> listarTodos() {
        return ResponseEntity.ok(sessaoService.listarTodas().stream()
                .map(SessaoConversacaoResponse::new)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obter(@PathVariable UUID id) {
        return sessaoService.obterSessao(id)
                .<ResponseEntity<Object>>map(s -> ResponseEntity.ok(new SessaoConversacaoResponse(s)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamento nÃ£o encontrado"));
    }

    @GetMapping("/modulo/{moduloId}")
    public ResponseEntity<Object> listarPorModulo(@PathVariable UUID moduloId) {
        return ResponseEntity.ok(sessaoService.listarPorModulo(moduloId).stream()
                .map(SessaoConversacaoResponse::new)
                .toList());
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<Object> listarPorProfessor(@PathVariable UUID professorId) {
        return ResponseEntity.ok(sessaoService.listarPorProfessor(professorId).stream()
                .map(SessaoConversacaoResponse::new)
                .toList());
    }

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody AbrirSessaoDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new SessaoConversacaoResponse(
                    sessaoService.abrirSessao(dto.professorId(), dto.moduloId(), dto.tipoSessao(), dto.modalidade(),
                            dto.dataHora(), dto.maxAlunos())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/alunos/{alunoId}")
    public ResponseEntity<Object> inscreverAluno(@PathVariable UUID id, @PathVariable UUID alunoId) {
        try {
            return ResponseEntity.ok(new SessaoConversacaoResponse(sessaoService.inscreverAluno(id, alunoId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarDataHora(
            @PathVariable UUID id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime novaDataHora) {
        try {
            return ResponseEntity.ok(new SessaoConversacaoResponse(sessaoService.atualizarDataHora(id, novaDataHora)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Object> finalizar(@PathVariable UUID id, @RequestBody FinalizarSessaoDto dto) {
        try {
            return ResponseEntity.ok(new SessaoConversacaoResponse(sessaoService.finalizarSessao(id, dto.gravacaoUrl())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/aluno/{alunoId}")
    public ResponseEntity<Object> removerAluno(@PathVariable UUID id, @PathVariable UUID alunoId) {
        try {
            sessaoService.removerAluno(id, alunoId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable UUID id) {
        sessaoService.deletarSessao(id);
        return ResponseEntity.noContent().build();
    }
}
