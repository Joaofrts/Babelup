package com.example.babelup.service;

import com.example.babelup.entities.Enum.EnumStatusProgresso;
import com.example.babelup.entities.usuarios.Aluno;
import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.entities.progressoGamificacao.ProgressoAluno;
import com.example.babelup.entities.usuarios.Usuario;
import com.example.babelup.repository.pedagogicos.ProgressoAlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProgressoService {

    private final ProgressoAlunoRepository progressoAlunoRepository;

    public ProgressoService(ProgressoAlunoRepository progressoAlunoRepository) {
        this.progressoAlunoRepository = progressoAlunoRepository;
    }


    @Transactional
    public ProgressoAluno atualizarProgressoModulo(Usuario aluno, Modulo modulo, Double novoPercentual) {

        ProgressoAluno progresso = progressoAlunoRepository.findByAlunoIdAndModuloId(aluno.getId(), modulo.getId())
                .orElseGet(() -> {
                    ProgressoAluno novo = new ProgressoAluno();
                    novo.setAluno((Aluno) aluno);
                    novo.setModulo(modulo);
                    return novo;
                });

        progresso.setPercentualConcluido(novoPercentual);
        progresso.setAtualizadoEm(LocalDateTime.now());

        if (novoPercentual >= 100.0) {
            progresso.setStatus(EnumStatusProgresso.CONCLUIDO);
        } else if (novoPercentual > 0.0) {
            progresso.setStatus(EnumStatusProgresso.EM_ANDAMENTO);
        }

        return progressoAlunoRepository.save(progresso);
    }

    public boolean moduloFoiConcluido(UUID alunoId, UUID moduloId) {
        Optional<ProgressoAluno> progressoOpt = progressoAlunoRepository.findByAlunoIdAndModuloId(alunoId, moduloId);

        if (progressoOpt.isEmpty()) {
            return false;
        }

        return progressoOpt.get().getStatus() == EnumStatusProgresso.CONCLUIDO;
    }

    public ProgressoAluno obterProgresso(UUID alunoId, UUID moduloId) {
        return progressoAlunoRepository.findByAlunoIdAndModuloId(alunoId, moduloId).orElse(null);
    }
}