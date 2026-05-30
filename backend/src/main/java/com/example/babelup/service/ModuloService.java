package com.example.babelup.service;

import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.entities.estruturaAcademica.Nivel;
import com.example.babelup.repository.pedagogicos.ModuloRepository;
import com.example.babelup.repository.pedagogicos.NivelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ModuloService {

    private final ModuloRepository moduloRepository;
    private final NivelRepository nivelRepository;
    private final ProgressoService progressoService;

    public ModuloService(ModuloRepository moduloRepository, NivelRepository nivelRepository, ProgressoService progressoService) {
        this.moduloRepository = moduloRepository;
        this.nivelRepository = nivelRepository;
        this.progressoService = progressoService;
    }


    @Transactional
    public Modulo criarModulo(UUID nivelId, String titulo, String descricao, Integer ordem, Integer cargaHorariaMinima) {
        Nivel nivel = nivelRepository.findById(nivelId)
                .orElseThrow(() -> new IllegalArgumentException("Nível não encontrado"));

        Modulo modulo = new Modulo();
        modulo.setNivel(nivel);
        modulo.setTitulo(titulo);
        modulo.setDescricao(descricao);
        modulo.setOrdem(ordem);
        modulo.setCargaHorariaMinima(cargaHorariaMinima);

        return moduloRepository.save(modulo);
    }

    public List<Modulo> listarModulos() {
        return moduloRepository.findAll();
    }

    public Optional<Modulo> obterModulo(UUID id) {
        return moduloRepository.findById(id);
    }

    public List<Modulo> obterModulosPorNivel(UUID nivelId) {
        return moduloRepository.findByNivelIdOrderByOrdemAsc(nivelId);
    }

    @Transactional
    public Modulo atualizarModulo(UUID id, String titulo, String descricao, Integer ordem, Integer cargaHorariaMinima) {
        Modulo modulo = moduloRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Módulo não encontrado com ID: " + id));

        modulo.setTitulo(titulo);
        modulo.setDescricao(descricao);
        modulo.setOrdem(ordem);
        modulo.setCargaHorariaMinima(cargaHorariaMinima);

        return moduloRepository.save(modulo);
    }

    @Transactional
    public void deletarModulo(UUID id) {
        moduloRepository.deleteById(id);
    }



    public Optional<Modulo> obterProximoModulo(UUID nivelId, Integer ordemAtual) {
        return moduloRepository.findByNivelIdAndOrdem(nivelId, ordemAtual + 1);
    }
}