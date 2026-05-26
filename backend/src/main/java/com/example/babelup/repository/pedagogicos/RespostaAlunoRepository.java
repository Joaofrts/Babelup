package com.example.babelup.repository.pedagogicos;

import com.example.babelup.entities.progressoGamificacao.RespostaAluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RespostaAlunoRepository extends JpaRepository<RespostaAluno, UUID> {
    List<RespostaAluno> findByAlunoId(UUID alunoId);

    int countByAlunoIdAndExercicioId(UUID alunoId, UUID exercicioId);

    List<RespostaAluno> findByAlunoIdAndExercicioIdOrderByTentativaAsc(UUID alunoId, UUID exercicioId);

    @Query("SELECT COUNT(DISTINCT r.exercicio.id) FROM RespostaAluno r WHERE r.aluno.id = :alunoId AND r.exercicio.videoAula.modulo.id = :moduloId AND r.correto = true")
    long countAcertosDistintosPorModuloEAluno(@Param("moduloId") UUID moduloId, @Param("alunoId") UUID alunoId);

    boolean existsByAlunoIdAndExercicioIdAndCorretoTrue(UUID alunoId, UUID exercicioId);
}
