package com.example.babelup.service;

import com.example.babelup.entities.Agendamento;
import com.example.babelup.entities.Modulo;
import com.example.babelup.entities.Usuario;
import com.example.babelup.repository.AgendamentoRepository;
import com.example.babelup.repository.ProgressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        novaSessao.getAlunos().add(aluno);

        return agendamentoRepository.save(novaSessao);
    }

}
