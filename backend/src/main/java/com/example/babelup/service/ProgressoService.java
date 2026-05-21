package com.example.babelup.service;

import com.example.babelup.entities.Aluno;
import com.example.babelup.entities.Modulo;
import com.example.babelup.entities.ProgressoAluno;
import com.example.babelup.entities.Usuario;
import com.example.babelup.repository.ProgressoRepository;
import com.example.babelup.repository.pedagogicos.ProgressoAlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProgressoService {

    @Autowired
    private ProgressoAlunoRepository progressoAlunoRepository;

    public ProgressoAluno registrarConclusaoModulo(Aluno aluno, Modulo modulo, boolean exercicioFeito, Double nota) {
        if (!exercicioFeito) {
            throw new IllegalStateException("O aluno precisa concluir o exercício integrado para finalizar o módulo.");
        }
        ProgressoAluno novoProgresso = new ProgressoAluno();
        novoProgresso.setAluno(aluno);
        novoProgresso.setModulo(modulo);
        novoProgresso.setExercicioConcluido(true);
        novoProgresso.setNotaExercicio(nota);
        novoProgresso.setDataConclusao(LocalDateTime.now());
        return progressoRepository.save(novoProgresso);
    }

    public boolean podeAcessarModulo(UUID alunoId, UUID moduloAtualId, UUID moduloAnteriorId) {
        if (moduloAnteriorId == null) {
            return true;
        }
        return progressoAlunoRepository.existsByAlunoIdAndModuloIdAndExercicioConcluidoTrue(alunoId, moduloAnteriorId);
    }

    public ProgressoAluno obterProgresso(UUID alunoId, UUID moduloId) {
        return progressoAlunoRepository.findByAlunoIdAndModuloId(alunoId, moduloId).orElse(null);
    }
}
