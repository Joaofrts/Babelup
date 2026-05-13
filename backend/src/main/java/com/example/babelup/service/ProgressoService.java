package com.example.babelup.service;

import com.example.babelup.entities.Modulo;
import com.example.babelup.entities.Progresso;
import com.example.babelup.entities.Usuario;
import com.example.babelup.repository.ProgressoRepository;

import java.time.LocalDateTime;

public class ProgressoService {

    private ProgressoRepository progressoRepository;

    public Progresso registrarConclusaoModulo(Usuario aluno, Modulo modulo, boolean exercicioFeito, Double nota) {
        if (!exercicioFeito) {
            throw new IllegalStateException("O aluno precisa concluir o exercício integrado para finalizar o módulo.");
        }
        Progresso novoProgresso = new Progresso();
        novoProgresso.setAluno(aluno);
        novoProgresso.setModulo(modulo);
        novoProgresso.setExercicioConcluido(true);
        novoProgresso.setNotaExercicio(nota);
        novoProgresso.setDataConclusao(LocalDateTime.now());
        return progressoRepository.save(novoProgresso);
    }

    public boolean podeAcessarModulo(Long alunoId, Long moduloAtualId, Long moduloAnteriorId) {
        if (moduloAnteriorId == null) {
            return true;
        }
        return progressoRepository.existsByAlunoIdAndModuloIdAndExercicioConcluidoTrue(alunoId, moduloAnteriorId);
    }
}
