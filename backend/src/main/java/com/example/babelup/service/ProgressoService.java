package com.example.babelup.service;

import com.example.babelup.entities.Modulo;
import com.example.babelup.entities.Progresso;
import com.example.babelup.entities.Usuario;
import com.example.babelup.repository.ProgressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProgressoService {

    @Autowired
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

    public Progresso obterProgresso(Long alunoId, Long moduloId) {
        return progressoRepository.findByAlunoIdAndModuloId(alunoId, moduloId).orElse(null);
    }
}
