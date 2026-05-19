package com.example.babelup.controller;

import com.example.babelup.dto.AgendamentoDto;
import com.example.babelup.entities.Agendamento;
import com.example.babelup.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    // GET /api/agendamentos - Listar todos os agendamentos
    @GetMapping
    public ResponseEntity<Object> listarTodos() {
        try {
            List<Agendamento> agendamentos = agendamentoService.listarTodos();
            List<AgendamentoDto> dtos = agendamentos.stream()
                    .map(this::converterParaDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar agendamentos: " + e.getMessage());
        }
    }

    // GET /api/agendamentos/{id} - Obter agendamento específico
    @GetMapping("/{id}")
    public ResponseEntity<Object> obterPorId(@PathVariable Long id) {
        try {
            Optional<Agendamento> agendamento = agendamentoService.obterPorId(id);
            if (agendamento.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamento não encontrado");
            }
            AgendamentoDto dto = converterParaDto(agendamento.get());
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter agendamento: " + e.getMessage());
        }
    }

    // GET /api/agendamentos/modulo/{moduloId} - Listar agendamentos de um módulo
    @GetMapping("/modulo/{moduloId}")
    public ResponseEntity<Object> listarPorModulo(@PathVariable Long moduloId) {
        try {
            List<Agendamento> agendamentos = agendamentoService.listarPorModulo(moduloId);
            List<AgendamentoDto> dtos = agendamentos.stream()
                    .map(this::converterParaDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar agendamentos do módulo: " + e.getMessage());
        }
    }

    // GET /api/agendamentos/professor/{professorId} - Listar agendamentos de um professor
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<Object> listarPorProfessor(@PathVariable Long professorId) {
        try {
            List<Agendamento> agendamentos = agendamentoService.listarPorProfessor(professorId);
            List<AgendamentoDto> dtos = agendamentos.stream()
                    .map(this::converterParaDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar agendamentos do professor: " + e.getMessage());
        }
    }

    // POST /api/agendamentos - Criar novo agendamento
    @PostMapping
    public ResponseEntity<Object> criarAgendamento(@RequestBody AgendamentoDto dto) {
        try {
            if (dto.getProfessorId() == null || dto.getModuloId() == null || 
                dto.getDataHora() == null || dto.getAlunoIds() == null || dto.getAlunoIds().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("professorId, moduloId, dataHora e alunoIds são obrigatórios");
            }

            Agendamento agendamento = agendamentoService.agendarSessaoCompleta(
                    dto.getProfessorId(), dto.getModuloId(), dto.getDataHora(), dto.getAlunoIds());
            
            AgendamentoDto resposta = converterParaDto(agendamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar agendamento: " + e.getMessage());
        }
    }

    // PUT /api/agendamentos/{id} - Atualizar agendamento
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarAgendamento(@PathVariable Long id, @RequestParam LocalDateTime novaDataHora) {
        try {
            Agendamento agendamento = agendamentoService.atualizar(id, novaDataHora);
            AgendamentoDto dto = converterParaDto(agendamento);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar agendamento: " + e.getMessage());
        }
    }

    // DELETE /api/agendamentos/{id}/aluno/{alunoId} - Remover aluno de um agendamento
    @DeleteMapping("/{id}/aluno/{alunoId}")
    public ResponseEntity<Object> removerAluno(@PathVariable Long id, @PathVariable Long alunoId) {
        try {
            agendamentoService.removerAluno(id, alunoId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao remover aluno: " + e.getMessage());
        }
    }

    // DELETE /api/agendamentos/{id} - Deletar agendamento
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarAgendamento(@PathVariable Long id) {
        try {
            agendamentoService.deletar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar agendamento: " + e.getMessage());
        }
    }

    // Método auxiliar para converter Agendamento para AgendamentoDto
    private AgendamentoDto converterParaDto(Agendamento agendamento) {
        List<Long> alunoIds = agendamento.getAlunos() != null 
                ? agendamento.getAlunos().stream().map(u -> u.getId()).collect(Collectors.toList())
                : List.of();
        
        Long professorId = agendamento.getProfessor() != null ? agendamento.getProfessor().getId() : null;
        Long moduloId = agendamento.getModulo() != null ? agendamento.getModulo().getId() : null;
        Integer qtdAlunos = agendamento.getAlunos() != null ? agendamento.getAlunos().size() : 0;
        
        return new AgendamentoDto(agendamento.getId(), agendamento.getDataHora(), 
                professorId, moduloId, alunoIds, qtdAlunos);
    }
}

