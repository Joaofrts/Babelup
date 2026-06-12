package com.example.babelup.service;

import com.example.babelup.dto.AdicionarNivelDto;
import com.example.babelup.dto.RespostaNivelDto;
import com.example.babelup.entities.estruturaAcademica.Nivel;
import com.example.babelup.repository.pedagogicos.NivelRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NivelService {

    private final NivelRepository nivelRepository;

    public NivelService(NivelRepository nivelRepository) {
        this.nivelRepository = nivelRepository;
    }

    public RespostaNivelDto criarNivel(AdicionarNivelDto dto) {
        if (nivelRepository.findByNomeAndIdioma(dto.nome(),dto.idioma()).isPresent()) {
            throw new IllegalArgumentException("Idioma '"+dto.idioma()+"' com nivel '" + dto.nome() + "' já existe");
        }
        Nivel nivelSalvo = nivelRepository.save(new Nivel(dto));
        return new RespostaNivelDto(nivelSalvo);
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
    @Transactional
    public RespostaNivelDto atualizarNivel(UUID id, AdicionarNivelDto dto) {
        Nivel n = nivelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nível não encontrado"));

        if (dto.idioma() != null && !dto.idioma().isBlank()) {
            n.setIdioma(dto.idioma());
        }

        if (dto.nome() != null && !dto.nome().isBlank()) {
            n.setNome(dto.nome());
        }

        if (dto.descricao() != null) {
            n.setDescricao(dto.descricao());
        }

        if (dto.cargaHoraria() != null) {
            n.setCargaHoraria(dto.cargaHoraria());
        }


        Nivel nivelAlterado = nivelRepository.save(n);
        return new RespostaNivelDto(nivelAlterado);
    }

    public void deletarNivel(UUID id) {
        nivelRepository.deleteById(id);
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
