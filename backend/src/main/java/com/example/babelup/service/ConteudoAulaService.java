package com.example.babelup.service;
import com.example.babelup.entities.Enum.EnumTipoVideo;
import com.example.babelup.entities.pratica.Exercicio;
import com.example.babelup.entities.pratica.MaterialApoio;
import com.example.babelup.entities.pratica.VideoAula;
import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.repository.pedagogicos.ExercicioRepository;
import com.example.babelup.repository.pedagogicos.MaterialApoioRepository;
import com.example.babelup.repository.pedagogicos.ModuloRepository;
import com.example.babelup.repository.pedagogicos.VideoAulaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ConteudoAulaService {

    private final ModuloRepository moduloRepository;
    private final VideoAulaRepository videoAulaRepository;
    private final MaterialApoioRepository materialApoioRepository;
    private final ExercicioRepository exercicioRepository;

    public ConteudoAulaService(ModuloRepository moduloRepository,
                               VideoAulaRepository videoAulaRepository,
                               MaterialApoioRepository materialApoioRepository,
                               ExercicioRepository exercicioRepository) {
        this.moduloRepository = moduloRepository;
        this.videoAulaRepository = videoAulaRepository;
        this.materialApoioRepository = materialApoioRepository;
        this.exercicioRepository = exercicioRepository;
    }

    @Transactional
    public VideoAula adicionarVideoAula(UUID moduloId, String titulo, String url, Integer duracao,
                                        EnumTipoVideo tipo, LocalDateTime dataTransmissao) {
        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new IllegalArgumentException("Módulo não encontrado."));

        VideoAula aula = new VideoAula();
        aula.setModulo(modulo);
        aula.setTitulo(titulo);
        aula.setUrl(url);
        aula.setDuracao(duracao);
        aula.setTipo(tipo);
        aula.setDataTransmissao(dataTransmissao);

        return videoAulaRepository.save(aula);
    }

    public List<VideoAula> listarAulasPorModulo(UUID moduloId) {
        return videoAulaRepository.findByModuloId(moduloId);
    }

    @Transactional
    public MaterialApoio adicionarMaterialApoio(UUID moduloId, String titulo, String urlPdf) {
        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new IllegalArgumentException("Módulo não encontrado."));

        MaterialApoio material = new MaterialApoio();
        material.setModulo(modulo);
        material.setTitulo(titulo);
        material.setUrlPdf(urlPdf);

        return materialApoioRepository.save(material);
    }

    public List<MaterialApoio> listarMateriaisPorModulo(UUID moduloId) {
        return materialApoioRepository.findByModuloId(moduloId);
    }


    @Transactional
    public Exercicio adicionarExercicioAula(UUID videoAulaId, String enunciado,
                                            String alternativasJson, String respostaCorreta) {
        VideoAula aula = videoAulaRepository.findById(videoAulaId)
                .orElseThrow(() -> new IllegalArgumentException("Videoaula não encontrada. O exercício deve ser vinculado a uma aula existente."));

        Exercicio exercicio = new Exercicio();
        exercicio.setVideoAula(aula);
        exercicio.setEnunciado(enunciado);
        exercicio.setAlternativas(alternativasJson);
        exercicio.setRespostaCorreta(respostaCorreta);

        return exercicioRepository.save(exercicio);
    }

    public List<Exercicio> listarExerciciosPorAula(UUID videoAulaId) {
        return exercicioRepository.findByVideoAulaId(videoAulaId);
    }
}