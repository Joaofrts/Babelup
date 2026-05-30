package com.example.babelup.service;

import com.example.babelup.dto.CursoCatalogoDTO;
import com.example.babelup.entities.estruturaAcademica.Nivel;
import com.example.babelup.repository.pedagogicos.NivelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogoService {

    private final NivelRepository nivelRepository;

    public CatalogoService(NivelRepository nivelRepository) {
        this.nivelRepository = nivelRepository;
    }

    @Transactional(readOnly = true)
    public List<CursoCatalogoDTO> listarCursosParaVitrine() {
        List<Nivel> niveisDisponiveis = nivelRepository.findAll();

        return niveisDisponiveis.stream()
                .map(nivel -> new CursoCatalogoDTO(
                        nivel.getId(),
                        nivel.getIdioma(),
                        nivel.getDescricao(),
                        nivel.getPrecoMensal()
                ))
                .collect(Collectors.toList());
    }
}
