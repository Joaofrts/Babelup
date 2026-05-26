package com.example.babelup.service;

import com.example.babelup.entities.progressoGamificacao.Ranking;
import com.example.babelup.entities.usuarios.Aluno;
import com.example.babelup.repository.sistema.RankingRepository;
import com.example.babelup.repository.usuarios.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class GamificacaoService {

    private final RankingRepository rankingRepository;
    private final AlunoRepository alunoRepository;

    public GamificacaoService(RankingRepository rankingRepository, AlunoRepository alunoRepository) {
        this.rankingRepository = rankingRepository;
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public Ranking adicionarPontos(UUID alunoId, Integer pontosGanhos) {

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        LocalDate hoje = LocalDate.now();
        int mesAtual = hoje.getMonthValue();
        int anoAtual = hoje.getYear();


        Ranking ranking = rankingRepository.findByAlunoIdAndMesAndAno(alunoId, mesAtual, anoAtual)
                .orElseGet(() -> {
                    Ranking novo = new Ranking();
                    novo.setAluno(aluno);
                    novo.setMes(mesAtual);
                    novo.setAno(anoAtual);
                    novo.setPontuacao(0);
                    novo.setPosicao(0);
                    return novo;
                });

        ranking.setPontuacao(ranking.getPontuacao() + pontosGanhos);

        return rankingRepository.save(ranking);
    }


    public List<Ranking> listarTop10DoMes(int mes, int ano) {
        return rankingRepository.findTop10ByMesAndAnoOrderByPontuacaoDesc(mes, ano);
    }

    @Transactional
    public void processarPosicoesDoMes(int mes, int ano) {

        List<Ranking> rankingsDoMes = rankingRepository.findByMesAndAnoOrderByPontuacaoDesc(mes, ano);

        int posicaoOficial = 1;
        for (Ranking r : rankingsDoMes) {
            r.setPosicao(posicaoOficial);
            posicaoOficial++;
        }

        rankingRepository.saveAll(rankingsDoMes);
    }
}
