package com.example.babelup.repository;

import com.example.babelup.entities.Progresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressoRepository extends JpaRepository<Progresso,Long> {
    List<Progresso> findByAlunoId(Long id);

    boolean existsByAlunoIdAndModuloIdAndExercicioConcluidoTrue(Long alunoId, Long moduloId);

    Optional<Progresso> findByAlunoIdAndModuloId(Long alunoId, Long moduloId);

    List<Progresso> findByModuloId(Long moduloId);

    List<Progresso> findByAlunoIdAndExercicioConcluidoTrue(Long alunoId);
}

