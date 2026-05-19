package com.example.babelup.service;

import com.example.babelup.entities.Modulo;
import com.example.babelup.entities.Nivel;
import com.example.babelup.entities.Progresso;
import com.example.babelup.repository.ModuloRepository;
import com.example.babelup.repository.NivelRepository;
import com.example.babelup.repository.ProgressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NivelService {

    @Autowired
    private NivelRepository nivelRepository;

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private ProgressoRepository progressoRepository;

    // Criar novo nível
    public Nivel criarNivel(String nome, Integer cargaHorariaEstimada) {
        if (nivelRepository.findByNome(nome).isPresent()) {
            throw new IllegalArgumentException("Nível com nome '" + nome + "' já existe");
        }
        Nivel nivel = new Nivel();
        nivel.setNome(nome);
        nivel.setCargaHorariaEstimada(cargaHorariaEstimada);
        return nivelRepository.save(nivel);
    }

    // Obter todos os níveis
    public List<Nivel> listarNiveis() {
        return nivelRepository.findAll();
    }

    // Obter nível por ID
    public Optional<Nivel> obterNivel(Long id) {
        return nivelRepository.findById(id);
    }

    // Atualizar nível
    public Nivel atualizarNivel(Long id, String nome, Integer cargaHorariaEstimada) {
        Optional<Nivel> nivel = nivelRepository.findById(id);
        if (nivel.isEmpty()) {
            throw new IllegalArgumentException("Nível não encontrado");
        }
        Nivel n = nivel.get();
        n.setNome(nome);
        n.setCargaHorariaEstimada(cargaHorariaEstimada);
        return nivelRepository.save(n);
    }

    // Deletar nível
    public void deletarNivel(Long id) {
        nivelRepository.deleteById(id);
    }

    // Validar se aluno pode progredir para o próximo nível
    // Todos os módulos do nível atual devem estar concluídos
    public boolean podeProgressarParaProximo(Long alunoId, Long nivelAtualId) {
        Nivel nivel = nivelRepository.findById(nivelAtualId)
                .orElseThrow(() -> new IllegalArgumentException("Nível não encontrado"));

        List<Modulo> modulosNivel = moduloRepository.findByNivelIdOrderByOrdemSequencialAsc(nivelAtualId);

        for (Modulo modulo : modulosNivel) {
            Optional<Progresso> progresso = progressoRepository.findByAlunoIdAndModuloId(alunoId, modulo.getId());
            
            // Se algum módulo não foi concluído, não pode progredir
            if (progresso.isEmpty() || !progresso.get().getExercicioConcluido()) {
                return false;
            }
        }

        return true;
    }

    // Obter próximo nível
    public Optional<Nivel> obterProximoNivel(Long nivelAtualId) {
        Optional<Nivel> nivelAtual = nivelRepository.findById(nivelAtualId);
        if (nivelAtual.isEmpty()) {
            return Optional.empty();
        }
        
        List<Nivel> todos = nivelRepository.findAll();
        // Simples: pega o próximo ID
        for (int i = 0; i < todos.size() - 1; i++) {
            if (todos.get(i).getId().equals(nivelAtualId)) {
                return Optional.of(todos.get(i + 1));
            }
        }
        return Optional.empty();
    }
}
