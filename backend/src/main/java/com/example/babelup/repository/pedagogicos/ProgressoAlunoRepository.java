package com.example.babelup.repository.pedagogicos;

import com.example.babelup.entities.enumEntities.EnumStatusProgresso;
import com.example.babelup.entities.progressoGamificacao.ProgressoAluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface ProgressoAlunoRepository extends JpaRepository<ProgressoAluno, UUID> {
    List<ProgressoAluno> findByAlunoId(UUID id);

    boolean existsByAlunoIdAndModuloIdAndStatus(UUID alunoId, UUID moduloId, EnumStatusProgresso status);

    Optional<ProgressoAluno> findByAlunoIdAndModuloId(UUID alunoId, UUID moduloId);

    List<ProgressoAluno> findByAlunoIdAndStatusAndUltimoAcessoBefore(UUID id, EnumStatusProgresso enumStatusProgresso, LocalDateTime dataLimite);
}