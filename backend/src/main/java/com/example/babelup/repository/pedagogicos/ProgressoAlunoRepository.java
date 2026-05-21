package com.example.babelup.repository.pedagogicos;

import com.example.babelup.entities.EnumStatusProgresso;
import com.example.babelup.entities.ProgressoAluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface ProgressoAlunoRepository extends JpaRepository<ProgressoAluno, UUID> {
    List<ProgressoAluno> findByAlunoId(UUID id);

    boolean existsByAlunoIdAndModuloIdAndStatus(UUID alunoId, UUID moduloId, EnumStatusProgresso status);

    Optional<ProgressoAluno> findByAlunoIdAndModuloId(UUID alunoId, UUID moduloId);
}