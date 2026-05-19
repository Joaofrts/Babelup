package com.example.babelup.service;

import com.example.babelup.entities.Agendamento;
import com.example.babelup.entities.Modulo;
import com.example.babelup.entities.Usuario;
import com.example.babelup.repository.AgendamentoRepository;
import com.example.babelup.repository.ModuloRepository;
import com.example.babelup.repository.ProgressoRepository;
import com.example.babelup.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProgressoRepository progressoRepository;

    private static final Integer LIMITE_ALUNOS = 5;

    // Agendar sessão de conversação (versão original simplificada)
    public Agendamento agendarSessao(Usuario aluno, Modulo modulo, Agendamento sessaoExistente) {
        boolean moduloConcluido = progressoRepository
                .existsByAlunoIdAndModuloIdAndExercicioConcluidoTrue(aluno.getId(), modulo.getId());

        if (!moduloConcluido) {
            throw new IllegalStateException("Conclua todas as aulas e exercícios do módulo antes de agendar a conversação.");
        }

        if (sessaoExistente != null) {
            if (sessaoExistente.getAlunos().size() >= LIMITE_ALUNOS) {
                throw new IllegalStateException("Esta sessão de conversação já atingiu o limite máximo de 5 alunos.");
            }
            if (!sessaoExistente.getAlunos().contains(aluno)) {
                sessaoExistente.getAlunos().add(aluno);
            }
            return agendamentoRepository.save(sessaoExistente);
        }

        Agendamento novaSessao = new Agendamento();
        novaSessao.setModulo(modulo);
        novaSessao.setAlunos(new ArrayList<>());
        novaSessao.getAlunos().add(aluno);

        return agendamentoRepository.save(novaSessao);
    }

    // Agendar sessão com validações completas (nova versão)
    public Agendamento agendarSessaoCompleta(Long professorId, Long moduloId, LocalDateTime dataHora, List<Long> alunoIds) {
        // Validar professor
        Usuario professor = usuarioRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado"));

        // Validar módulo
        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new IllegalArgumentException("Módulo não encontrado"));

        // Validar se todos os alunos podem agendar (exercício concluído)
        validarAlunosPermitidos(alunoIds, moduloId);

        // Buscar alunos
        List<Usuario> alunos = new ArrayList<>();
        for (Long alunoId : alunoIds) {
            Usuario aluno = usuarioRepository.findById(alunoId)
                    .orElseThrow(() -> new IllegalArgumentException("Aluno com ID " + alunoId + " não encontrado"));
            alunos.add(aluno);
        }

        // Tentar reutilizar agendamento existente
        List<Agendamento> agendamentosExistentes = agendamentoRepository.findByModuloId(moduloId);
        for (Agendamento agenda : agendamentosExistentes) {
            if (agenda.getAlunos().size() < LIMITE_ALUNOS) {
                // Adicionar novos alunos à sessão existente
                for (Usuario aluno : alunos) {
                    if (!agenda.getAlunos().contains(aluno)) {
                        agenda.getAlunos().add(aluno);
                    }
                }
                return agendamentoRepository.save(agenda);
            }
        }

        // Criar nova sessão
        Agendamento novoAgendamento = new Agendamento();
        novoAgendamento.setProfessor(professor);
        novoAgendamento.setModulo(modulo);
        novoAgendamento.setDataHora(dataHora);
        novoAgendamento.setAlunos(new ArrayList<>(alunos));

        return agendamentoRepository.save(novoAgendamento);
    }

    // Validar se todos os alunos podem agendar
    // Pré-requisito: exercicioConcluido = true
    private void validarAlunosPermitidos(List<Long> alunoIds, Long moduloId) {
        for (Long alunoId : alunoIds) {
            boolean temExercicioConcluido = progressoRepository
                    .existsByAlunoIdAndModuloIdAndExercicioConcluidoTrue(alunoId, moduloId);

            if (!temExercicioConcluido) {
                throw new IllegalStateException(
                    "Aluno com ID " + alunoId + " não concluiu o exercício do módulo " + moduloId
                );
            }
        }
    }

    // Listar todos os agendamentos
    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    // Obter agendamento por ID
    public Optional<Agendamento> obterPorId(Long id) {
        return agendamentoRepository.findById(id);
    }

    // Listar agendamentos de um módulo
    public List<Agendamento> listarPorModulo(Long moduloId) {
        return agendamentoRepository.findByModuloId(moduloId);
    }

    // Listar agendamentos de um professor
    public List<Agendamento> listarPorProfessor(Long professorId) {
        return agendamentoRepository.findByProfessorId(professorId);
    }

    // Remover aluno de um agendamento
    public void removerAluno(Long agendamentoId, Long alunoId) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));

        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

        agendamento.getAlunos().remove(aluno);
        agendamentoRepository.save(agendamento);
    }

    // Atualizar agendamento
    public Agendamento atualizar(Long id, LocalDateTime novaDataHora) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));

        agendamento.setDataHora(novaDataHora);
        return agendamentoRepository.save(agendamento);
    }

    // Deletar agendamento
    public void deletar(Long id) {
        agendamentoRepository.deleteById(id);
    }
}

