package com.example.babelup.service;

import com.example.babelup.dto.CursoCatalogoDTO;
import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.repository.pedagogicos.ModuloRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogoService {

    private final ModuloRepository moduloRepository;

    public CatalogoService(ModuloRepository moduloRepository) {
        this.moduloRepository = moduloRepository;
    }

    @Transactional(readOnly = true)
    public List<CursoCatalogoDTO> listarCursosParaVitrine() {
        List<Modulo> modulosDisponiveis = moduloRepository.findAll();

        return modulosDisponiveis.stream()
                .map(modulo -> new CursoCatalogoDTO(
                        modulo.getId(),
                        modulo.getTitulo(),
                        modulo.getDescricao(),
                        modulo.getPrecoMensal()
                ))
                .collect(Collectors.toList());
    }
}
