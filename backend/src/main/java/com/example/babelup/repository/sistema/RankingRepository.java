package com.example.babelup.repository.sistema;

import com.example.babelup.entities.progressoGamificacao.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, UUID> {
    Optional<Ranking> findByAlunoIdAndMesAndAno(UUID alunoId, int mesAtual, int anoAtual);

    List<Ranking> findTop10ByMesAndAnoOrderByPontuacaoDesc(int mes, int ano);

    List<Ranking> findByMesAndAnoOrderByPontuacaoDesc(int mes, int ano);
}
