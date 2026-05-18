package com.example.babelup.service;

import com.example.babelup.entities.Agendamento;
import com.example.babelup.entities.Modulo;
import com.example.babelup.entities.Usuario;
import com.example.babelup.repository.AgendamentoRepository;
import com.example.babelup.repository.ProgressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {
    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ProgressoRepository progressoRepository;

    public Agendamento agendarSessao(Usuario aluno, Modulo modulo, Agendamento sessaoExistente) {

        boolean moduloConcluido = progressoRepository
                .existsByAlunoIdAndModuloIdAndExercicioConcluidoTrue(aluno.getId(), modulo.getId());

        if (!moduloConcluido) {
            throw new IllegalStateException("Conclua todas as aulas e exercícios do módulo antes de agendar a conversação.");
        }

        if (sessaoExistente != null) {
            if (sessaoExistente.getAlunos().size() >= 5) {
                throw new IllegalStateException("Esta sessão de conversação já atingiu o limite máximo de 5 alunos.");
            }
            sessaoExistente.getAlunos().add(aluno);
            return agendamentoRepository.save(sessaoExistente);
        }

        Agendamento novaSessao = new Agendamento();
        novaSessao.setModulo(modulo);
        novaSessao.setAlunos(new ArrayList<>());
        novaSessao.getAlunos().add(aluno);

        return agendamentoRepository.save(novaSessao);
    }

    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    public Optional<Agendamento> buscarPorId(Long id) {
        return agendamentoRepository.findById(id);
    }

    public List<Agendamento> listarPorModulo(Long moduloId) {
        return agendamentoRepository.findByModuloId(moduloId);
    }

    public Agendamento removerAluno(Long agendamentoId, Long alunoId) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new IllegalStateException("Agendamento não encontrado"));

        agendamento.getAlunos().removeIf(u -> u.getId().equals(alunoId));
        return agendamentoRepository.save(agendamento);
    }

    public Agendamento atualizar(Agendamento agendamento) {
        if (!agendamentoRepository.existsById(agendamento.getId())) {
            throw new IllegalStateException("Agendamento não encontrado");
        }
        return agendamentoRepository.save(agendamento);
    }

    public void deletar(Long id) {
        if (!agendamentoRepository.existsById(id)) {
            throw new IllegalStateException("Agendamento não encontrado");
        }
        agendamentoRepository.deleteById(id);
    }

}
