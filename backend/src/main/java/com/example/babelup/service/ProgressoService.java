package com.example.babelup.service;

import com.example.babelup.entities.enumEntities.EnumStatusProgresso;
import com.example.babelup.entities.usuarios.Aluno;
import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.entities.progressoGamificacao.ProgressoAluno;
import com.example.babelup.entities.usuarios.Usuario;
import com.example.babelup.repository.pedagogicos.ModuloRepository;
import com.example.babelup.repository.pedagogicos.ProgressoAlunoRepository;
import com.example.babelup.repository.usuarios.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProgressoService {

    private final ProgressoAlunoRepository progressoAlunoRepository;
    private final ModuloRepository moduloRepository;
    private final AlunoRepository alunoRepository;

    public ProgressoService(ProgressoAlunoRepository progressoAlunoRepository,
                            ModuloRepository moduloRepository,
                            AlunoRepository alunoRepository) {
        this.progressoAlunoRepository = progressoAlunoRepository;
        this.moduloRepository = moduloRepository;
        this.alunoRepository = alunoRepository;
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

        ProgressoAluno progressoSalvo = progressoAlunoRepository.save(progresso);

        if (aluno instanceof Aluno alunoEntity) {
            atualizarProgressoGeralDoNivel(alunoEntity);
        }

        return progressoSalvo;
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

    @Transactional(readOnly = true)
    public int calcularProgressoNivel(Aluno aluno) {
        if (aluno == null || aluno.getNivelAtual() == null || aluno.getNivelAtual().getId() == null) {
            return 0;
        }

        List<Modulo> modulos = moduloRepository.findByNivelIdOrderByOrdemAsc(aluno.getNivelAtual().getId());

        if (modulos.isEmpty()) {
            return 0;
        }

        double somaPercentuais = modulos.stream()
                .mapToDouble(modulo -> progressoAlunoRepository
                        .findByAlunoIdAndModuloId(aluno.getId(), modulo.getId())
                        .map(ProgressoAluno::getPercentualConcluido)
                        .orElse(0.0))
                .sum();

        return (int) Math.round(somaPercentuais / modulos.size());
    }

    @Transactional
    public int atualizarProgressoGeralDoNivel(Aluno aluno) {
        int progressoGeral = calcularProgressoNivel(aluno);
        aluno.setProgressoGeral((double) progressoGeral);
        alunoRepository.save(aluno);
        return progressoGeral;
    }
}
