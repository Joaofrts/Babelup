package com.example.babelup.service;

import com.example.babelup.entities.Modulo;
import com.example.babelup.entities.Progresso;
import com.example.babelup.entities.Usuario;
import com.example.babelup.repository.ProgressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProgressoService {

    @Autowired
    private ProgressoRepository progressoRepository;

    public Progresso registrarConclusaoModulo(Usuario aluno, Modulo modulo, boolean exercicioFeito, Double nota) {
        if (!exercicioFeito) {
            throw new IllegalStateException("O aluno precisa concluir o exercício integrado para finalizar o módulo.");
        }
        if (nota < 7.0) {
            throw new IllegalStateException("Nota insuficiente para conclusão do módulo. Mínimo: 7.0");
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

    public Progresso marcarAulaAssistida(Long alunoId, Modulo modulo, Usuario aluno) {
        Optional<Progresso> progressoExistente = progressoRepository.findByAlunoIdAndModuloId(alunoId, modulo.getId());
        Progresso progresso;
        
        if (progressoExistente.isPresent()) {
            progresso = progressoExistente.get();
        } else {
            progresso = new Progresso();
            progresso.setAluno(aluno);
            progresso.setModulo(modulo);
        }
        
        return progressoRepository.save(progresso);
    }

    public Progresso submeterExercicio(Long alunoId, Modulo modulo, Usuario aluno, Double nota) {
        if (nota == null || nota < 0 || nota > 10) {
            throw new IllegalStateException("Nota deve estar entre 0 e 10");
        }
        
        Optional<Progresso> progressoExistente = progressoRepository.findByAlunoIdAndModuloId(alunoId, modulo.getId());
        Progresso progresso;
        
        if (progressoExistente.isPresent()) {
            progresso = progressoExistente.get();
        } else {
            progresso = new Progresso();
            progresso.setAluno(aluno);
            progresso.setModulo(modulo);
        }
        
        progresso.setNotaExercicio(nota);
        return progressoRepository.save(progresso);
    }

    public Progresso submeterTesteFinal(Long alunoId, Modulo modulo, Usuario aluno, Double nota) {
        if (nota == null || nota < 0 || nota > 10) {
            throw new IllegalStateException("Nota deve estar entre 0 e 10");
        }
        
        Optional<Progresso> progressoExistente = progressoRepository.findByAlunoIdAndModuloId(alunoId, modulo.getId());
        Progresso progresso;
        
        if (progressoExistente.isPresent()) {
            progresso = progressoExistente.get();
        } else {
            progresso = new Progresso();
            progresso.setAluno(aluno);
            progresso.setModulo(modulo);
        }
        
        if (nota >= 7.0) {
            progresso.setExercicioConcluido(true);
            progresso.setDataConclusao(LocalDateTime.now());
        }
        
        progresso.setNotaExercicio(nota);
        return progressoRepository.save(progresso);
    }

    public Optional<Progresso> obterProgresso(Long alunoId, Long moduloId) {
        return progressoRepository.findByAlunoIdAndModuloId(alunoId, moduloId);
    }

    public boolean verificarModuloConcluido(Long alunoId, Long moduloId) {
        return progressoRepository.existsByAlunoIdAndModuloIdAndExercicioConcluidoTrue(alunoId, moduloId);
    }
}
