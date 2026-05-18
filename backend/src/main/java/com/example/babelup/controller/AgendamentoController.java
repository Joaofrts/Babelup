package com.example.babelup.controller;

import com.example.babelup.dto.AgendamentoDto;
import com.example.babelup.entities.Agendamento;
import com.example.babelup.entities.Modulo;
import com.example.babelup.entities.Usuario;
import com.example.babelup.repository.ModuloRepository;
import com.example.babelup.repository.UsuarioRepository;
import com.example.babelup.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModuloRepository moduloRepository;

    @PostMapping("/agendar")
    public ResponseEntity<Object> agendar(@RequestBody AgendamentoRequest request) {
        try {
            Usuario aluno = usuarioRepository.findById(request.getAlunoId())
                    .orElseThrow(() -> new IllegalStateException("Aluno não encontrado"));

            Modulo modulo = moduloRepository.findById(request.getModuloId())
                    .orElseThrow(() -> new IllegalStateException("Módulo não encontrado"));

            Agendamento sessaoExistente = null;
            if (request.getAgendamentoId() != null) {
                sessaoExistente = agendamentoService.buscarPorId(request.getAgendamentoId())
                        .orElseThrow(() -> new IllegalStateException("Agendamento não encontrado"));
            }

            Agendamento agendamento = agendamentoService.agendarSessao(aluno, modulo, sessaoExistente);
            return ResponseEntity.ok().body(new AgendamentoDto(agendamento));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao agendar sessão: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> listarTodos() {
        try {
            List<Agendamento> agendamentos = agendamentoService.listarTodos();
            List<AgendamentoDto> dtos = agendamentos.stream()
                    .map(AgendamentoDto::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar agendamentos");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id) {
        try {
            Agendamento agendamento = agendamentoService.buscarPorId(id)
                    .orElseThrow(() -> new IllegalStateException("Agendamento não encontrado"));
            return ResponseEntity.ok().body(new AgendamentoDto(agendamento));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar agendamento");
        }
    }

    @GetMapping("/modulo/{moduloId}")
    public ResponseEntity<Object> listarPorModulo(@PathVariable Long moduloId) {
        try {
            moduloRepository.findById(moduloId)
                    .orElseThrow(() -> new IllegalStateException("Módulo não encontrado"));

            List<Agendamento> agendamentos = agendamentoService.listarPorModulo(moduloId);
            List<AgendamentoDto> dtos = agendamentos.stream()
                    .map(AgendamentoDto::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(dtos);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar agendamentos do módulo");
        }
    }

    @DeleteMapping("/{id}/aluno/{alunoId}")
    public ResponseEntity<Object> removerAluno(@PathVariable Long id, @PathVariable Long alunoId) {
        try {
            usuarioRepository.findById(alunoId)
                    .orElseThrow(() -> new IllegalStateException("Aluno não encontrado"));

            Agendamento agendamento = agendamentoService.removerAluno(id, alunoId);
            return ResponseEntity.ok().body(new AgendamentoDto(agendamento));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao remover aluno do agendamento");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable Long id, @RequestBody Agendamento agendamento) {
        try {
            agendamento.setId(id);
            Agendamento atualizado = agendamentoService.atualizar(agendamento);
            return ResponseEntity.ok().body(new AgendamentoDto(atualizado));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar agendamento");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable Long id) {
        try {
            agendamentoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar agendamento");
        }
    }

    @GetMapping("/buscar-sessao-disponivel/{moduloId}")
    public ResponseEntity<Object> buscarSessaoDisponivel(@PathVariable Long moduloId) {
        try {
            List<Agendamento> agendamentos = agendamentoService.listarPorModulo(moduloId);
            Agendamento sessaoDisponivel = agendamentos.stream()
                    .filter(a -> a.getAlunos().size() < 5)
                    .findFirst()
                    .orElse(null);

            if (sessaoDisponivel == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma sessão disponível para este módulo");
            }

            return ResponseEntity.ok().body(new AgendamentoDto(sessaoDisponivel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar sessão disponível");
        }
    }

    static class AgendamentoRequest {
        private Long alunoId;
        private Long moduloId;
        private Long agendamentoId;

        public AgendamentoRequest() {}

        public Long getAlunoId() {
            return alunoId;
        }

        public void setAlunoId(Long alunoId) {
            this.alunoId = alunoId;
        }

        public Long getModuloId() {
            return moduloId;
        }

        public void setModuloId(Long moduloId) {
            this.moduloId = moduloId;
        }

        public Long getAgendamentoId() {
            return agendamentoId;
        }

        public void setAgendamentoId(Long agendamentoId) {
            this.agendamentoId = agendamentoId;
        }
    }
}

