package com.example.babelup.service;

import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.entities.estruturaAcademica.Nivel;
import com.example.babelup.entities.progressoGamificacao.ProgressoAluno;
import com.example.babelup.repository.pedagogicos.ModuloRepository;
import com.example.babelup.repository.pedagogicos.NivelRepository;
import com.example.babelup.repository.pedagogicos.ProgressoAlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NivelService {

    @Autowired
    private NivelRepository nivelRepository;

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private ProgressoAlunoRepository progressoAlunoRepository;

    public Nivel criarNivel(String nome, Integer cargaHorariaEstimada) {
        if (nivelRepository.findByNome(nome).isPresent()) {
            throw new IllegalArgumentException("Nível com nome '" + nome + "' já existe");
        }
        Nivel nivel = new Nivel();
        nivel.setNome(nome);
        nivel.setCargaHoraria(cargaHorariaEstimada);
        return nivelRepository.save(nivel);
    }

    // Obter todos os níveis
    public List<Nivel> listarNiveis() {
        return nivelRepository.findAll();
    }

    // Obter nível por ID
    public Optional<Nivel> obterNivel(UUID id) {
        return nivelRepository.findById(id);
    }

    // Atualizar nível
    public Nivel atualizarNivel(UUID id, String nome, Integer cargaHorariaEstimada) {
        Optional<Nivel> nivel = nivelRepository.findById(id);
        if (nivel.isEmpty()) {
            throw new IllegalArgumentException("Nível não encontrado");
        }
        Nivel n = nivel.get();
        n.setNome(nome);
        n.setCargaHoraria(cargaHorariaEstimada);
        return nivelRepository.save(n);
    }

    public void deletarNivel(UUID id) {
        nivelRepository.deleteById(id);
    }

    public boolean podeProgressarParaProximo(UUID alunoId, UUID nivelAtualId) {
        Nivel nivel = nivelRepository.findById(nivelAtualId)
                .orElseThrow(() -> new IllegalArgumentException("Nível não encontrado"));

        List<Modulo> modulosNivel = moduloRepository.findByNivelIdOrderByOrdemAsc(nivelAtualId);

        for (Modulo modulo : modulosNivel) {
            Optional<ProgressoAluno> progresso = progressoAlunoRepository.findByAlunoIdAndModuloId(alunoId, modulo.getId());

            if (progresso.isEmpty() || !progresso.get().isConcluido()) {
                return false;
            }
        }

        return true;
    }

    // Obter próximo nível
    public Optional<Nivel> obterProximoNivel(UUID nivelAtualId) {
        Optional<Nivel> nivelAtual = nivelRepository.findById(nivelAtualId);
        if (nivelAtual.isEmpty()) {
            return Optional.empty();
        }
        
        List<Nivel> todos = nivelRepository.findAll();
        // Simples: pega o próximo ID
        for (int i = 0; i < todos.size() - 1; i++) {
            if (todos.get(i).getOrdem().equals(nivelAtual.get().getOrdem())) {
                return Optional.of(todos.get(i + 1));
            }
        }
        return Optional.empty();
    }
}
