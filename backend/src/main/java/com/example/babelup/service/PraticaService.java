package com.example.babelup.service;
import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.entities.pratica.Exercicio;
import com.example.babelup.entities.progressoGamificacao.RespostaAluno;
import com.example.babelup.entities.usuarios.Aluno;
import com.example.babelup.repository.pedagogicos.ExercicioRepository;
import com.example.babelup.repository.pedagogicos.RespostaAlunoRepository;
import com.example.babelup.repository.usuarios.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PraticaService {

    private final RespostaAlunoRepository respostaAlunoRepository;
    private final ExercicioRepository exercicioRepository;
    private final AlunoRepository alunoRepository;
    private final ProgressoService progressoService;
    private final GamificacaoService gamificacaoService;

    public PraticaService(RespostaAlunoRepository respostaAlunoRepository,
                          ExercicioRepository exercicioRepository,
                          AlunoRepository alunoRepository,
                          ProgressoService progressoService,
                          GamificacaoService gamificacaoService) {
        this.respostaAlunoRepository = respostaAlunoRepository;
        this.exercicioRepository = exercicioRepository;
        this.alunoRepository = alunoRepository;
        this.progressoService = progressoService;
        this.gamificacaoService = gamificacaoService;
    }

    @Transactional
    public RespostaAluno submeterResposta(UUID alunoId, UUID exercicioId, String respostaSubmetida) {

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        Exercicio exercicio = exercicioRepository.findById(exercicioId)
                .orElseThrow(() -> new IllegalArgumentException("Exercício não encontrado."));

        boolean jaAcertouAntes = respostaAlunoRepository.existsByAlunoIdAndExercicioIdAndCorretoTrue(alunoId, exercicioId);


        int tentativasAnteriores = respostaAlunoRepository.countByAlunoIdAndExercicioId(alunoId, exercicioId);
        int tentativaAtual = tentativasAnteriores + 1;


        boolean isCorreto = respostaSubmetida.trim().equalsIgnoreCase(exercicio.getRespostaCorreta().trim());


        RespostaAluno novaResposta = new RespostaAluno();
        novaResposta.setAluno(aluno);
        novaResposta.setExercicio(exercicio);
        novaResposta.setResposta(respostaSubmetida);
        novaResposta.setCorreto(isCorreto);
        novaResposta.setTentativa(tentativaAtual);

        RespostaAluno respostaSalva = respostaAlunoRepository.save(novaResposta);

        if (isCorreto && !jaAcertouAntes) {
            Modulo modulo = exercicio.getVideoAula().getModulo();

            long totalExercicios = exercicioRepository.countByVideoAulaModuloId(modulo.getId());

            long exerciciosAcertados = respostaAlunoRepository.countAcertosDistintosPorModuloEAluno( modulo.getId(), aluno.getId());

            double novoPercentual = (totalExercicios == 0) ? 100.0 : ((double) exerciciosAcertados / totalExercicios) * 100.0;

            progressoService.atualizarProgressoModulo(aluno, modulo, novoPercentual);

            gamificacaoService.adicionarPontos(alunoId, 10);
        }

        return respostaSalva;
    }

    public List<RespostaAluno> listarHistoricoDeRespostas(UUID alunoId, UUID exercicioId) {
        return respostaAlunoRepository.findByAlunoIdAndExercicioIdOrderByTentativaAsc(alunoId, exercicioId);
    }
}