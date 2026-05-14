package com.example.babelup.repository;

import com.example.babelup.entities.Progresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressoRepository extends JpaRepository<Progresso,Long> {
    List<Progresso> findByAlunoId(Long id);

    boolean existsByAlunoIdAndModuloIdAndExercicioConcluidoTrue(Long alunoId,Long moduloId);
}
