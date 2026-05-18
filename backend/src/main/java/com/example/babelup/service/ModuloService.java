package com.example.babelup.service;

import com.example.babelup.entities.Modulo;
import com.example.babelup.entities.Progresso;
import com.example.babelup.repository.ModuloRepository;
import com.example.babelup.repository.ProgressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModuloService {

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private ProgressoRepository progressoRepository;

    public List<Modulo> listarModulosPorNivel(Long nivelId) {
        return moduloRepository.findByNivelIdOrderByOrdemSequencialAsc(nivelId);
    }

    public List<Modulo> listarModulosDisponiveis(Long alunoId, Long nivelId) {
        List<Modulo> todosModulos = listarModulosPorNivel(nivelId);
        
        return todosModulos.stream()
                .filter(modulo -> podeAcessarModulo(alunoId, modulo))
                .collect(Collectors.toList());
    }

    public boolean podeAcessarModulo(Long alunoId, Modulo modulo) {
        // Primeiro módulo é sempre acessível
        if (modulo.getOrdemSequencial() == null || modulo.getOrdemSequencial() <= 1) {
            return true;
        }

        // Buscar módulo anterior
        Optional<Modulo> moduloAnterior = moduloRepository
                .findByNivelIdOrderByOrdemSequencialAsc(modulo.getNivel().getId())
                .stream()
                .filter(m -> m.getOrdemSequencial() != null && 
                            m.getOrdemSequencial().equals(modulo.getOrdemSequencial() - 1))
                .findFirst();

        if (moduloAnterior.isEmpty()) {
            return false;
        }

        // Verificar se aluno completou módulo anterior
        return progressoRepository.existsByAlunoIdAndModuloIdAndExercicioConcluidoTrue(
                alunoId, 
                moduloAnterior.get().getId()
        );
    }

    public Optional<Modulo> obterProximoModulo(Long alunoId, Long moduloId) {
        Modulo moduloAtual = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new IllegalStateException("Módulo não encontrado"));

        // Verificar se módulo atual foi concluído
        if (!progressoRepository.existsByAlunoIdAndModuloIdAndExercicioConcluidoTrue(alunoId, moduloId)) {
            throw new IllegalStateException("Você ainda não completou este módulo");
        }

        return moduloRepository.findNextModule(moduloAtual.getNivel().getId(), moduloAtual.getOrdemSequencial());
    }

    public boolean verificarPodeAgendarConversacao(Long alunoId, Long moduloId) {
        return progressoRepository.existsByAlunoIdAndModuloIdAndExercicioConcluidoTrue(alunoId, moduloId);
    }

    public Modulo obterPorId(Long moduloId) {
        return moduloRepository.findById(moduloId)
                .orElseThrow(() -> new IllegalStateException("Módulo não encontrado"));
    }

    public Modulo atualizar(Modulo modulo) {
        if (!moduloRepository.existsById(modulo.getId())) {
            throw new IllegalStateException("Módulo não encontrado");
        }
        return moduloRepository.save(modulo);
    }
}
