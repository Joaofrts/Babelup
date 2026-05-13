package com.example.babelup.repository;

import com.example.babelup.entities.Progresso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressoRepository extends JpaRepository<Progresso,Long> {
    List<Progresso> findByAlunoId();

    boolean existsByAlunoIdAndModuloIdAndExercicioConcluidoTrue(Long alunoId,Long moduloId);
}
